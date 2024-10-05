package com.group1.Care_Koi_System.entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Pond_Feeding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pond_id")
    private Ponds ponds;

    @ManyToOne
    @JoinColumn(name = "feeding_id")
    private Feeding feeding;
}
