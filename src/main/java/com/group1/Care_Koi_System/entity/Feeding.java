package com.group1.Care_Koi_System.entity;

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
}
