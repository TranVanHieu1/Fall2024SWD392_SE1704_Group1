package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.config.VNPayConfig;
import com.group1.Care_Koi_System.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentService {

    @Autowired
    private VNPayConfig vnpayConfig;

    public RedirectView createPayment(Payment payment) {
        String vnp_TxnRef = String.valueOf(new Date().getTime()); // Mã giao dịch
        String vnp_OrderInfo = "Thanh toan don hang: " + payment.getId();
        String vnp_IpAddr = "127.0.0.1"; // Địa chỉ IP test
        String vnp_Amount = String.valueOf(payment.getTotalPrice() * 100); // Chuyển sang VND (x100)

        // Tạo các tham số gửi đến VNPay
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
        params.put("vnp_Amount", vnp_Amount);
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", vnp_TxnRef);
        params.put("vnp_OrderInfo", vnp_OrderInfo);
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
        params.put("vnp_IpAddr", vnp_IpAddr);
        params.put("vnp_CreateDate", getFormattedDate());

        String queryUrl = buildQueryString(params);
        String secureHash = hmacSHA512(vnpayConfig.getSecretKey(), queryUrl);
        String paymentUrl = vnpayConfig.getPaymentUrl() + "?" + queryUrl + "&vnp_SecureHash=" + secureHash;

        return new RedirectView(paymentUrl);
    }

    private String buildQueryString(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String value = params.get(fieldName);
            if (value != null && value.length() > 0) {
                query.append(fieldName).append('=').append(value).append('&');
            }
        }
        query.deleteCharAt(query.length() - 1); // Xóa dấu '&' cuối cùng
        return query.toString();
    }

    private String getFormattedDate() {
        Calendar calendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
    }
    private String hmacSHA512(String key, String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hmac = md.digest((key + data).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * hmac.length);
            for (byte b : hmac) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
