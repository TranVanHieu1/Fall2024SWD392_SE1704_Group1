package com.group1.Care_Koi_System.dto.Account;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String message;

    private String accessToken;

    private String refreshToken;

    public LoginResponse(String message, String accessToken, String refreshToken) {
        super();
        this.message = message;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
