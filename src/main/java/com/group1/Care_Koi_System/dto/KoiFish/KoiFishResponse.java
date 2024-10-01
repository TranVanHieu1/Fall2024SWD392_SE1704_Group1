package com.group1.Care_Koi_System.dto.KoiFish;

import com.group1.Care_Koi_System.entity.Enum.HealthyStatus;
import com.group1.Care_Koi_System.entity.Enum.KoiGender;
import com.group1.Care_Koi_System.entity.Enum.KoiOrigin;
import com.group1.Care_Koi_System.entity.Enum.KoiSpecies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KoiFishResponse {
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

    private String message;

    public KoiFishResponse(String message){
        this.message = message;
    }

}
