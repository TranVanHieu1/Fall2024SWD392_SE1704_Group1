package com.group1.Care_Koi_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder

@Entity
public class Ponds {

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

//    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
//    @JsonIgnore
//    @Transient
//    private List<Pond_KoiFish> pondKoiFishs;

//    @ManyToMany
//    @JoinTable(
//    name = "pond_koifish",
//    joinColumns = @JoinColumn(name = "pond_id"),
//    inverseJoinColumns = @JoinColumn(name = "koifish_id")
//    )
//    private List<KoiFish> koiFishList = new ArrayList<>();

    @OneToMany(mappedBy = "ponds", cascade = CascadeType.ALL)
    @Transient
    private List<KoiFish> koiFishList;

    @OneToMany(mappedBy = "pond")
    @Transient
    private List<WaterParameter> parameters;

}
