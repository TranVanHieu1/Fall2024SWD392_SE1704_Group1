package com.group1.Care_Koi_System.entity;

import com.group1.Care_Koi_System.entity.Enum.CategoryItem;
import com.group1.Care_Koi_System.entity.Enum.ServiceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "item_name")
    private String itemName;

    @Column
    private double price;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryItem category;

    @Column
    private int quantity;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    @Column
    private boolean isDelete;
}
