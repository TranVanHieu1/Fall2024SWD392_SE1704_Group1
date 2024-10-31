package com.group1.Care_Koi_System.dto.Payment;

import com.group1.Care_Koi_System.entity.Enum.PaymentMethodEnum;
import com.group1.Care_Koi_System.entity.Enum.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private int id;
    private LocalDateTime paymentDate;
    private long totalPrice;
    private String details;
//    private int orderId;
    private PaymentStatus status;
    private PaymentMethodEnum paymentMethod;
}
