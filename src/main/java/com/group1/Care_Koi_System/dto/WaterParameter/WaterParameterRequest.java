package com.group1.Care_Koi_System.dto.WaterParameter;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WaterParameterRequest {

    private String percentSalt;

    private String temperature;

    private String pH;

    private String O2;

    private String NO2;

    private String NO3;

}
