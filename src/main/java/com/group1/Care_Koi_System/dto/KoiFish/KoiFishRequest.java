package com.group1.Care_Koi_System.dto.KoiFish;


import com.group1.Care_Koi_System.entity.Enum.HealthyStatus;
import com.group1.Care_Koi_System.entity.Enum.KoiGender;
import com.group1.Care_Koi_System.entity.Enum.KoiOrigin;
import com.group1.Care_Koi_System.entity.Enum.KoiSpecies;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KoiFishRequest {

    private String fishName;

    private String imageFish;

    private int age;

    private double size;

    private double weigh;

    private String note;
}
