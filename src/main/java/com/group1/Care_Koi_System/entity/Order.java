package com.group1.Care_Koi_System.entity;

import com.group1.Care_Koi_System.entity.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column
    private String note;

    @Column
    private boolean isDelete;

    @OneToMany(mappedBy = "order")
    @Transient
    private List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    @Transient
    private Payment payment;
}
