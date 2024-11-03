package com.group1.Care_Koi_System.controller;


import com.group1.Care_Koi_System.dto.ApiRes;
import com.group1.Care_Koi_System.dto.HistoryResponse;
import com.group1.Care_Koi_System.dto.Pond.DayChangeWaterResponse;
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
    public ResponseEntity<ResponseException> createPond(@RequestBody @Valid PondRequest request) {
        return pondService.createPond(request);
    }


    @PutMapping("update/{pondId}")
    public ResponseEntity<ResponseException> updatePond(@PathVariable int pondId, @RequestBody PondRequest request) {
        return pondService.updatePond(pondId, request);
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



    @GetMapping("view-pond-by-id/{pondID}")
    public ResponseEntity<?> viewPondById(@PathVariable int pondID){
        return pondService.getPondByID(pondID);
    }

    @GetMapping("change-water/{pondID}")
    public ResponseEntity<DayChangeWaterResponse> getDayChangWater(@PathVariable int pondID){
        return  pondService.calculateDayChangeWater(pondID);
    }

    @GetMapping("history-change-water/{pondID}")
    public  ResponseEntity<HistoryResponse> viewHistoryChangeWater(@PathVariable int pondID){
        return  pondService.getHistoryPond(pondID);
    }
}

