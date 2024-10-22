package com.group1.Care_Koi_System.service;


import com.group1.Care_Koi_System.dto.Payment.PaymentRequest;
import com.group1.Care_Koi_System.dto.Payment.PaymentResponse;
import com.group1.Care_Koi_System.entity.Account;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    String vnp_TmnCode = "C0DZPJH8";
    String vnp_HashSecret = "ZPQF7HG5PUJZVPPUG09WT6VFQ7X9GQAQ";
    String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    // Sử dụng vnp_ReturnUrl từ PaymentService1
    String vnp_ReturnUrl = "http://localhost:8081/return-url";
    private final Map<String, String> paymentTokens = new HashMap<>();

    public List<PaymentResponse> getPaymentHistory() {
        return paymentRepository.findAll().stream()
                .map(payment -> new PaymentResponse(
                        payment.getId(),
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
            int id = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(request.getTotal_price() * 100));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", "NCB");
            vnp_Params.put("vnp_TxnRef", String.valueOf(System.currentTimeMillis()));
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang");
            vnp_Params.put("vnp_OrderType", "billpayment");
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", request.getIpAddr());

            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
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
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            paymentTokens.put(String.valueOf(id), vnp_SecureHash);
            return vnp_Url + "?" + queryUrl;
        } catch (Exception e) {
            throw new RuntimeException("Payment failed!");
        }
    }
    public boolean verifyPayment(Map<String, String> params) {
        try {
            String vnp_SecureHash = params.get("vnp_SecureHash").toUpperCase();
            List<String> fieldNames = new ArrayList<>(params.keySet());
            fieldNames.remove("vnp_SecureHash");
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString())).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String checkSecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            return vnp_SecureHash.equals(checkSecureHash);
        } catch (Exception e) {
            log.error("Payment verification failed!");
            return false;
        }
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac.init(secretKeySpec);
        byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString().toUpperCase();
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
