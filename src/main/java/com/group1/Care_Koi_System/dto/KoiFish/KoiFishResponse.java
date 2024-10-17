package com.group1.Care_Koi_System.dto.KoiFish;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group1.Care_Koi_System.dto.Pond.PondResponse;
import com.group1.Care_Koi_System.entity.Enum.HealthyStatus;
import com.group1.Care_Koi_System.entity.Enum.KoiGender;
import com.group1.Care_Koi_System.entity.Enum.KoiOrigin;
import com.group1.Care_Koi_System.entity.Enum.KoiSpecies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KoiFishResponse {
    private int id;

    private String fishName;

    private String imageFish;

    private int age;

    private KoiSpecies species;

    private double size;

    private double weigh;

    private KoiGender gender;

    private KoiOrigin origin;

    private HealthyStatus healthyStatus;

    private String note;

    private int pondID;

    private LocalDateTime dateAdded;

    private List<PondResponse> ponds;
    private String message;
    public KoiFishResponse(int id, String fishName, String imageFish, int age, KoiSpecies species, double size, double weigh,
                           KoiGender gender, KoiOrigin origin, HealthyStatus healthyStatus, String note, int pondID) {
        this.id = id;
        this.fishName = fishName;
        this.imageFish = imageFish;
        this.age = age;
        this.species = species;
        this.size = size;
        this.weigh = weigh;
        this.gender = gender;
        this.origin = origin;
        this.healthyStatus = healthyStatus;
        this.note = note;
        this.pondID = pondID;
    }


    public KoiFishResponse(String message) {
        this.message = message;
    }

}
