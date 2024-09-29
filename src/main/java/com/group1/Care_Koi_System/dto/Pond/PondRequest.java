package com.group1.Care_Koi_System.dto.Pond;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PondRequest {
    private String namePond;
    private String image;
    private double size;
    private double volume;
}
