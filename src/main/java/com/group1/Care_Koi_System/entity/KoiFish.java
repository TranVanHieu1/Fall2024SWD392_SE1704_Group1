package com.group1.Care_Koi_System.entity;

import com.group1.Care_Koi_System.entity.Enum.HealthyStatus;
import com.group1.Care_Koi_System.entity.Enum.KoiGender;
import com.group1.Care_Koi_System.entity.Enum.KoiOrigin;
import com.group1.Care_Koi_System.entity.Enum.KoiSpecies;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KoiFish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String fishName;

    @Column
    private String imageFish;

    @Column
    private int age;

    @Column
    private KoiSpecies species;

    @Column
    private double size;

    @Column
    private double weigh;

    @Column
    @Enumerated(EnumType.STRING)
    private KoiGender gender;

    @Column
    @Enumerated(EnumType.STRING)
    private KoiOrigin origin;

    @Column
    @Enumerated(EnumType.STRING)
    private HealthyStatus healthyStatus;

    @Column(name = "time_feeding")
    private LocalDateTime timeFeeding;

    @Column(name = "amount_of_food")
    private double amountOfFood;

    @Column
    private String note;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "koiFish", cascade = CascadeType.ALL)
    @Transient
    private List<Pond_KoiFish> pondKoiFish;



}
