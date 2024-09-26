package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.Account.LoginRequest;
import com.group1.Care_Koi_System.dto.Account.LoginResponse;
import com.group1.Care_Koi_System.dto.Account.RegisReponse;
import com.group1.Care_Koi_System.dto.Account.RegisterRequest;
import com.group1.Care_Koi_System.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@CrossOrigin("*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String a(){
        return "Heleo";
    }

    @PostMapping("/register")
    public RegisReponse registerAccount(@RequestBody @Valid RegisterRequest registerRequest){
        return  accountService.registerAccount(registerRequest);

    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequestDTO) {
        return accountService.checkLogin(loginRequestDTO);
    }

}
