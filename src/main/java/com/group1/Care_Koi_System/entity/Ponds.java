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
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
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

    @Column
    private int maximum;

    @Column
    private Integer numberChangeWater;

    @Column
    private String dateAutoFilter;

    @ElementCollection
    @CollectionTable(name = "pond_change_history", joinColumns = @JoinColumn(name = "pond_id"))
    @Column(name = "change_history")
    private List<String> changeHistory = new ArrayList<>();

    @OneToMany(mappedBy = "ponds")
    @JsonIgnore
    private List<Pond_Feeding> pondFeedingList;

    @OneToMany(mappedBy = "ponds", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Pond_KoiFish> koiFishList;

    @OneToMany(mappedBy = "ponds", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Ticket> listTicket;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<WaterParameter> parameters;

    @Column
    private LocalDateTime createAt;


    public void addChangeHistory(String message) {
        this.changeHistory.add(message);
    }


}
