package com.group1.Care_Koi_System.dto.Pond;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ViewPondResponse {
    private int id;

    private String namePond;

    private List<String> fishname;

    private String image;

    private double pondSize;

    private double height;

    private double volume;

    private String temper;

    private String salt;

    private String O2;

    private String NO2;

    private String NO3;

    private String PH;

    public ViewPondResponse(int id, String namePond, List<String> fishname, String image, double pondSize, double volume, double height) {
        this.id = id;
        this.namePond = namePond;
        this.fishname = fishname;
        this.image = image;
        this.pondSize = pondSize;
        this.volume = volume;
        this.height = height;
    }

    public ViewPondResponse(String namePond, List<String> fishname, double pondSize, double volume, String temper, String salt, String o2, String NO2, String NO3, String PH) {
        this.namePond = namePond;
        this.fishname = fishname;
        this.pondSize = pondSize;
        this.volume = volume;
        this.temper = temper;
        this.salt = salt;
        O2 = o2;
        this.NO2 = NO2;
        this.NO3 = NO3;
        this.PH = PH;
    }
}


