package com.group1.Care_Koi_System.service;


import com.group1.Care_Koi_System.dto.Payment.PaymentRequest;
import com.group1.Care_Koi_System.dto.Payment.PaymentResponse;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Enum.PaymentMethodEnum;
import com.group1.Care_Koi_System.entity.Enum.PaymentStatus;
import com.group1.Care_Koi_System.entity.Payment;
import com.group1.Care_Koi_System.repository.AccountRepository;
import com.group1.Care_Koi_System.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    String vnp_TmnCode = "3N379WTD";
    String vnp_HashSecret = "1CDS4VIWW8VWI65MZT5QV7ZN4TDADUXF";
    String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    // Sử dụng vnp_ReturnUrl từ PaymentService1
    String vnp_ReturnUrl = "http://localhost:8081/return-url";
    private final Map<String, String> paymentTokens = new HashMap<>();

    public List<PaymentResponse> getPaymentHistory() {
        return paymentRepository.findAll().stream()
                .map(payment -> new PaymentResponse(
                        payment.getId(),
                        payment.getOrder().getId(),
                        payment.getPaymentDate(),
                        payment.getTotalPrice(),
                        payment.getDetails(),
                        payment.getStatus(),
                        payment.getPaymentMethod()
                ))
                .collect(Collectors.toList());
    }

    public String createPayment(PaymentRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(request.getTotalPrice() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_Params.get("vnp_TxnRef"));
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", request.getIpAddr());

            Calendar cld = Calendar.getInstance(java.util.TimeZone.getTimeZone("Etc/GMT+7"));
            String vnp_CreateDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = new SimpleDateFormat("yyyyMMddHHmmss").format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }

            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            Account account = accountRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

            // Lưu token của người dùng với transaction reference
            paymentTokens.put(vnp_Params.get("vnp_TxnRef"), account.getUsername());
            return vnp_Url + "?" + queryUrl;
        } catch (Exception e) {
            // Thêm log thông tin về lỗi
            e.printStackTrace(); // Log lỗi vào console để xem chi tiết hơn
            throw new RuntimeException("Payment failed due to: " + e.getMessage());
        }

    }

    public boolean verifyPayment(Map<String, String> params) throws UnsupportedEncodingException {
        String vnp_SecureHash = params.get("vnp_SecureHash").toUpperCase();
        params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        try {
            String hashString = hmacSHA512(vnp_HashSecret, hashData.toString());
            if (!hashString.equals(vnp_SecureHash)) {
                log.error("Secure hash mismatch. Expected: {}, Actual: {}", hashString, vnp_SecureHash);
                return false;
            }
        } catch (Exception e) {
            log.error("Error while verifying payment hash", e);
            return false;
        }
        String responseCode = params.get("vnp_ResponseCode");
        if ("00".equals(responseCode)) {
            System.out.println("Payment success!");
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            long amount = Long.parseLong(params.get("vnp_Amount")) / 100;
            System.out.println("Amount: " + amount);

            Payment payment = new Payment();
            payment.setId(account.getId());
            payment.setPaymentDate(LocalDateTime.now());
            payment.setTotalPrice(amount);
            payment.setPaymentMethod(PaymentMethodEnum.valueOf(params.get("vnp_PayType")));
            payment.setDetails(params.toString());
            payment.setStatus(PaymentStatus.valueOf("SUCCESS"));
            paymentRepository.save(payment);

            accountRepository.save(account);
            return true;
        } else {
            log.info("Response code is not 00. Actual response: {}", responseCode);
        }
        return false;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512_HMAC.init(secret_key);
        return bytesToHex(sha512_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


}
