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
public class KoiFish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @ManyToOne
//    @JoinColumn(name = "pond_id")
//    private Ponds ponds;

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

    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @Column
    @Enumerated(EnumType.STRING)
    private HealthyStatus healthyStatus;

    @Column
    private String note;

    @OneToMany(mappedBy = "koiFish", cascade = CascadeType.ALL)
    private List<Pond_KoiFish> pondKoiFish;



}
