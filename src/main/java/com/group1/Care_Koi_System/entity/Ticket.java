package com.group1.Care_Koi_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pond_id")
    private Ponds ponds; //

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "fish_id")
    private KoiFish koiFish;

    @Column
    private boolean isResolved;

    @Column
    private String text;

    @Column
    private boolean isDeleted;


}
