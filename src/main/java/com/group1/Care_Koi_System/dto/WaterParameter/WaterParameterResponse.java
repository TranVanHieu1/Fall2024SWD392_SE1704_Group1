package com.group1.Care_Koi_System.dto.WaterParameter;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WaterParameterResponse {

    private String percentSalt;

    private String temperature;

    private String pH;

    private String O2;

    private String NO2;

    private String NO3;

    private LocalDateTime checkDate;

    private String message;

    public WaterParameterResponse(String message) {
        this.message = message;
    }
}
