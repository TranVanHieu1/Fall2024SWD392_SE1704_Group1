package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.Payment.PaymentRequest;
import com.group1.Care_Koi_System.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody PaymentRequest request) {
        try {
            String paymentUrl = paymentService.createPayment(request);
            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment: " + e.getMessage());
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestParam Map<String, String> params) {
        try {
            boolean isSuccess = paymentService.verifyPayment(params);
            if (isSuccess) {
                return ResponseEntity.ok("Payment success!");
            } else {
                return ResponseEntity.ok("Payment verification failed!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment verification failed: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory() {
        try {
            return ResponseEntity.ok(paymentService.getPaymentHistory());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error getting payment history: " + e.getMessage());
        }
    }

}
