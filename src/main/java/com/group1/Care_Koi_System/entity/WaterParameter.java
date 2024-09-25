package com.group1.Care_Koi_System.entity;


import com.group1.Care_Koi_System.entity.Enum.WaterParameterEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder

@Entity
public class WaterParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pond_id")
    private Ponds pond;

    @Column
    private LocalDateTime checkDate;

    @Column
    private double amountOfSalt;

    @Column
    private double temperature;

    @Column
    @Enumerated(EnumType.STRING)
    private WaterParameterEnum parameter;
}
