package com.group1.Care_Koi_System.entity;

import com.group1.Care_Koi_System.entity.Enum.PaymentMethodEnum;
import com.group1.Care_Koi_System.entity.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "details")
    private String details;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;
}
