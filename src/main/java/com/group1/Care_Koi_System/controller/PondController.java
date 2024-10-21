package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.ApiRes;
import com.group1.Care_Koi_System.dto.Pond.PondRequest;
import com.group1.Care_Koi_System.dto.Pond.PondResponse;
import com.group1.Care_Koi_System.dto.SearchPond.PondSearchResponse;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.service.PondService;
import com.group1.Care_Koi_System.utils.AccountUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("**")
@RequestMapping("/ponds")
public class PondController {

    @Autowired
    private  PondService pondService;
    @Autowired
    private  AccountUtils accountUtils;


    @PostMapping("/create-pond")
    public ResponseEntity<ApiRes<PondResponse>> createPond(@RequestBody @Valid PondRequest request) {

        Account account = accountUtils.getCurrentAccount();
        Ponds pond = pondService.createPond(request, account.getId());
        ApiRes<PondResponse> apiRes = new ApiRes<>();
        apiRes.setMessage("Create Pond SuccessFully");

        return ResponseEntity.status(HttpStatus.CREATED).body(apiRes);
    }


    @PutMapping("update/{pondId}")
    public ResponseEntity<ApiRes<PondResponse>> updatePond(@PathVariable int pondId, @RequestBody PondRequest request) {

        int accountId = accountUtils.getCurrentAccount().getId();
        Ponds updatedPond = pondService.updatePond(pondId, request, accountId);

        ApiRes<PondResponse> apiRes = new ApiRes<>();
        apiRes.setMessage("Update Successfully!");

        return ResponseEntity.ok(apiRes);
    }

    @DeleteMapping("/delete-pond/{pondID}")
    public ResponseEntity<ResponseException> removePond(@PathVariable int pondID){
        return pondService.deletePond(pondID);
    }

    @GetMapping("/view-pond-by-account")
    public ResponseEntity<?> viewPondByAccount(){
        return pondService.getPondsByAccount();
    }

    @GetMapping("/get-all-ponds")
    public ResponseEntity<?> viewAllPonds(){
        return  pondService.getAllPonds();
    }

}

