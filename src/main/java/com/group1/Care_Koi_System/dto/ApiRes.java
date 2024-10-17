package com.group1.Care_Koi_System.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRes<T> {
    private Boolean status;
    private Integer code;
    private String message;
    private T data;


}
