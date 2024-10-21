package com.group1.Care_Koi_System.dto.Account;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashBoardResponse {

    private int numberPond;

    private int numberFish;

    private int numberUser;

    private String message;

    public DashBoardResponse(int numberPond, int numberFish) {
        this.numberPond = numberPond;
        this.numberFish = numberFish;
    }

    public DashBoardResponse(int numberPond, int numberFish, int numberUser) {
        this.numberPond = numberPond;
        this.numberFish = numberFish;
        this.numberUser = numberUser;
    }

    public DashBoardResponse(String message) {
        this.message = message;
    }
}
