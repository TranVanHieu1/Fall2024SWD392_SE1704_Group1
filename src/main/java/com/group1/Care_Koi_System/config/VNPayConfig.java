package com.group1.Care_Koi_System.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class VNPayConfig {

    @Value("${vnpay.vnp_TmnCode}")
    private String vnp_TmnCode;
    @Value("${vnpay.accessKey}")
    private String accessKey;

    @Value("${vnpay.secretKey}")
    private String secretKey;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    @Value("${vnpay.paymentUrl}")
    private String paymentUrl;
}
