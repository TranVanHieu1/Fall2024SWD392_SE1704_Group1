package com.group1.Care_Koi_System.entity;

import com.group1.Care_Koi_System.entity.Enum.CategoryItem;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "item_name")
    private String itemName;

    @Column
    private double price;

    @Column
    @Enumerated(EnumType.STRING)
    private CategoryItem category;

    @Column
    private int quantity;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime updateAt;

    @Column
    private String imageFood;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "foodItem")
    @Transient
    private List<OrderDetail> orderDetails;
}
