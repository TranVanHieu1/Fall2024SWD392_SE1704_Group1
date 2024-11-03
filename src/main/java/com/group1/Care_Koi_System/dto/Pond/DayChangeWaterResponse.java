package com.group1.Care_Koi_System.dto.Pond;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DayChangeWaterResponse {

    private String dayChangeWater;

}
