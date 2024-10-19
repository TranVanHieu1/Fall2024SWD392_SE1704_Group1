package com.group1.Care_Koi_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ponds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
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
    private double height;

    @Column
    private boolean isDeleted;

    @OneToMany(mappedBy = "ponds")
    @JsonIgnore
    private List<Pond_Feeding> pondFeedingList;

    @OneToMany(mappedBy = "ponds", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Pond_KoiFish> koiFishList;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<WaterParameter> parameters;


    @Column
    private LocalDateTime createAt;
}
