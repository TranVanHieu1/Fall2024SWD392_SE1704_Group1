package com.group1.Care_Koi_System.dto.Account;

import com.group1.Care_Koi_System.entity.Enum.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class UpdateAccountRequest {
    private String useName;

    private String address;

    private GenderEnum gender;

    private String phone;
}
