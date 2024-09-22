package com.group1.Care_Koi_System.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder

@Entity
public class Pond {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    private String namePond;

    @Column
    private String image;

    @Column
    private double size;

    @Column
    private double volume;

    @OneToMany(mappedBy = "pond")
    @Transient
    private List<Feeding> feedingLis;

    @ManyToMany(mappedBy = "ponds")
    @Transient
    private List<KoiFish> koiFishList;

    @OneToMany(mappedBy = "pond")
    @Transient
    private List<WaterParameter> parameters;

}
