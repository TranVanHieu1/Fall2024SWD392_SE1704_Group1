package com.group1.Care_Koi_System.dto.WaterParameter;


import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WaterParameterResponse {

    private String percentSalt;

    private String temperature;

    private String pH;

    private String O2;

    private String NO2;

    private String NO3;

    private LocalDateTime checkDate;
}
