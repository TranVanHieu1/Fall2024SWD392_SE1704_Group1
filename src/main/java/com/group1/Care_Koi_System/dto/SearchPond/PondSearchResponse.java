package com.group1.Care_Koi_System.dto.SearchPond;

import com.group1.Care_Koi_System.dto.Pond.PondResponse;
import com.group1.Care_Koi_System.entity.Pond_KoiFish;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.entity.WaterParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PondSearchResponse {
    private String message;
    private List<PondResponse> pondsList;
}
