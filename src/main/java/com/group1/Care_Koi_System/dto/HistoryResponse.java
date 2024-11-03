package com.group1.Care_Koi_System.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data

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

    private List<String> historyChangeWater;

    public HistoryResponse(String message) {
        this.message = message;
    }

    public HistoryResponse(int fishID, String name, LocalDateTime addDate, LocalDateTime endDate, String message) {
        this.fishID = fishID;
        this.name = name;
        this.addDate = addDate;
        this.endDate = endDate;
        this.message = message;
    }

    public HistoryResponse(List<String> historyChangeWater) {
        this.historyChangeWater = historyChangeWater;
    }
}
