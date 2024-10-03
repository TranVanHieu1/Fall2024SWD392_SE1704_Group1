package com.group1.Care_Koi_System.entity;


import com.group1.Care_Koi_System.entity.Enum.WaterParameterEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WaterParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pond_id")
    private Ponds pond;

    @Column
    private LocalDateTime checkDate;

    @Column(name = "percent_salt")
    private String percentSalt;

    @Column
    private String temperature;

    @Column
    private String pH;

    @Column
    private String O2;

    @Column
    private String NO2;

    @Column
    private String NO3;

    @Column
    private boolean isDeleted;
}
