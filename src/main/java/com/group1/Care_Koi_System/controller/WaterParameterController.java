package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterRequest;
import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterResponse;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.service.WaterParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/water-parameter")
@CrossOrigin("**")
public class WaterParameterController {


    @Autowired
    private WaterParameterService waterParameterService;

    @PostMapping("/add-parameter/{pondID}")
    public ResponseEntity<ResponseException> addParameter(@PathVariable int pondID, @RequestBody WaterParameterRequest waterParameterRequest){
        return  waterParameterService.createWaterParameter(pondID, waterParameterRequest);
    }

    @PutMapping("/update-parameter/{pondID}")
    public ResponseEntity<ResponseException> updateParameter(@PathVariable int pondID, @RequestBody WaterParameterRequest waterParameterRequest){
        return  waterParameterService.updateWaterParameter(pondID, waterParameterRequest);
    }

    @GetMapping("view-water-parameter-by-pond/{pondID}")
    public ResponseEntity<WaterParameterResponse> viewWaterParameter(@PathVariable int pondID){
        return waterParameterService.getWaterParameterByPond(pondID);
    }

    @GetMapping("number-day-auto-filter-water/{pondID}")
    public  ResponseEntity<WaterParameterResponse> autoFilter(@PathVariable int pondID){
        return waterParameterService.getAutoFilter(pondID);
    }

    @PostMapping("execute-auto-filter/{pondID}")
    public ResponseEntity<ResponseException>  filter(@PathVariable int pondID){
        return waterParameterService.autoWaterFiltering(pondID);
    }

    @GetMapping("get-all-number-day-autou-filter-water")
    public ResponseEntity<?> getAutoFilter(){
        return  waterParameterService.getAllAutoFilter();
    }
}
