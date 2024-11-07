package com.group1.Care_Koi_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.group1.Care_Koi_System.entity.Enum.FoodType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Feeding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "feeding")
    private List<Pond_Feeding> pondFeedingList;

    @Column(name = "food_type")
    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    @Column
    private double amount;

    @Column(name = "feeding_time")
    private LocalDateTime feedingTime;

    public Feeding(int id, FoodType foodType, double amount, LocalDateTime feedingTime) {
        this.id = id;
        this.foodType = foodType;
        this.amount = amount;
        this.feedingTime = feedingTime;
    }
}
