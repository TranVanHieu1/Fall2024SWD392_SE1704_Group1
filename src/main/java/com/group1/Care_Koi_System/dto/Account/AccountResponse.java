package com.group1.Care_Koi_System.dto.Account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.group1.Care_Koi_System.entity.Enum.AccountRole;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private int id;

    private String username;

    private String email;

    private String address;

    private String phone;

    private AccountRole role;

}
