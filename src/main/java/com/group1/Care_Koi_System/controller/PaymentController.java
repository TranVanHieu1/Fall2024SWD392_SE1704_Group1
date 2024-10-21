package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.entity.Payment;
import com.group1.Care_Koi_System.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public RedirectView createPayment(@RequestBody Payment payment) {
        return paymentService.createPayment(payment);
    }

}
