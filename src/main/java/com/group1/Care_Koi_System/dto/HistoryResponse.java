package com.group1.Care_Koi_System.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HistoryResponse {
    private int fishID;

    private String name;

    private LocalDateTime addDate;

    private LocalDateTime endDate;

    private String message;

    public HistoryResponse(String message) {
        this.message = message;
    }

    public HistoryResponse(int fishID, String name, LocalDateTime addDate, LocalDateTime endDate) {
        this.fishID = fishID;
        this.name = name;
        this.addDate = addDate;
        this.endDate = endDate;
    }
}
