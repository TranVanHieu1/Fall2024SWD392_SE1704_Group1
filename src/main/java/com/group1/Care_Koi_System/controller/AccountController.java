package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.Account.*;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@CrossOrigin("**")
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

    @GetMapping("/get-all-account")
    public ResponseEntity<List<AccountResponse>> viewAccount() {
        return accountService.getAllAccount();
    }

    @DeleteMapping("/delete-account/{accountID}")
    public ResponseEntity<ResponseException> removeAccount(@PathVariable int accountID){
        return accountService.deleteAccount(accountID);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ResponseException> updateProfile(@RequestBody UpdateAccountRequest updateAccountRequest){
        return accountService.updateAccount(updateAccountRequest);
    }

    @GetMapping("/get-account/{accountID}")
    public  ResponseEntity<AccountResponse> getAccountByID(@PathVariable int accountID){
        return  accountService.getAnAccount(accountID);
    }

    @GetMapping("/dash-board")
    public ResponseEntity<DashBoardResponse> getDashBoard(){
        return  accountService.dashBoard();
    }
}
