package com.group1.Care_Koi_System.dto.Pond;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewPondResponse {
    private int id;

    private String namePond;

    private List<String> fishname;

    private String image;

    private double pondSize;

    private double volume;

}
