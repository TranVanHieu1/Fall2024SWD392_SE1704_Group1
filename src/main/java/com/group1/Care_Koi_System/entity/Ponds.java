package com.group1.Care_Koi_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "ponds")
    @Transient
    private List<Pond_Feeding> pondFeedingList;

    @OneToMany(mappedBy = "ponds", cascade = CascadeType.ALL)
    @Transient
    private List<Pond_KoiFish> koiFishList;

    @OneToMany(mappedBy = "pond")
    @Transient
    private List<WaterParameter> parameters;

    @Column
    private LocalDateTime createAt;
}
