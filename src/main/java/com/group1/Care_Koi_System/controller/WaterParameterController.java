package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterRequest;
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

    @PostMapping("/add-parameter/{Id}")
    public ResponseEntity<ResponseException> addParameter(@PathVariable int Id, @RequestBody WaterParameterRequest waterParameterRequest){
        return  waterParameterService.createWaterParameter(Id, waterParameterRequest);
    }
}
