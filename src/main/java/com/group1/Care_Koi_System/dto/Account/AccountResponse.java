package com.group1.Care_Koi_System.dto.Account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group1.Care_Koi_System.entity.Enum.AccountRole;
import lombok.*;

@Setter
@Getter

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {

    private int id;

    private String username;

    private String email;

    private String address;

    private String phone;

    private AccountRole role;

    private String message;

    public AccountResponse(int id, String username, String email, String address, String phone, AccountRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.role = role;
    }

    public AccountResponse(String message) {
        this.message = message;
    }
}
