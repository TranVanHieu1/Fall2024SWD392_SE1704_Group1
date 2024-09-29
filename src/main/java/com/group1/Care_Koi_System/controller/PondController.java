package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.ApiRes;
import com.group1.Care_Koi_System.dto.Pond.PondRequest;
import com.group1.Care_Koi_System.dto.Pond.PondResponse;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.service.PondService;
import com.group1.Care_Koi_System.utils.AccountUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/ponds")
public class PondController {

    private final PondService pondService;
    private final AccountUtils accountUtils;

    @Autowired
    public PondController(PondService pondService, AccountUtils accountUtils) {
        this.pondService = pondService;
        this.accountUtils = accountUtils;
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<ApiRes<PondResponse>> createPond(@Valid @RequestBody PondRequest request) {

        Account account = accountUtils.getCurrentAccount();
        Ponds pond = pondService.createPond(request, account.getId());
        ApiRes<PondResponse> apiRes = new ApiRes<>();
        apiRes.setMessage("Create Pond SuccessFully");


        return ResponseEntity.status(HttpStatus.CREATED).body(apiRes);
    }
}

