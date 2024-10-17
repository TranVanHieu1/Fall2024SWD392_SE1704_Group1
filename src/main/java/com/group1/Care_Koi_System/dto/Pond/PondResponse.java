package com.group1.Care_Koi_System.dto.Pond;


import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterResponse;
import com.group1.Care_Koi_System.entity.KoiFish;
import com.group1.Care_Koi_System.entity.Pond_KoiFish;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.entity.WaterParameter;
import lombok.*;

import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PondResponse {

    private String message;
    private int id;
    private String namePond;
    private double size;
    private double volume;
    private String image;
    private LocalDateTime createAt;
    private List<KoiFishResponse> koiFishList;
    private List<WaterParameterResponse> parameters;
}
