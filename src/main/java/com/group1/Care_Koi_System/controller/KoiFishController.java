package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.KoiFish.KoiFishRequest;
import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.service.KoiFishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/koifish")
@CrossOrigin("**")
public class KoiFishController {
    @Autowired
    private KoiFishService koiFishService;


    @PostMapping("/create-fish/{pondID}")
    public ResponseEntity<KoiFishResponse> addKoiFish( @PathVariable int pondID, @RequestBody KoiFishRequest koiFishRequest){
        return koiFishService.createKoiFish(koiFishRequest, pondID);
    }
}
