package com.group1.Care_Koi_System.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pond_KoiFish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "pond_id", nullable = false)
    private Ponds ponds;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "koi_fish_id")
    private KoiFish koiFish;

    @Column(name = "date_added")
    private LocalDateTime dateAdded;

    @Column(name ="end_date")
    private LocalDateTime endDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "message")
    private String message;
}
