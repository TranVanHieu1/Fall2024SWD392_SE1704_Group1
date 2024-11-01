package com.group1.Care_Koi_System.controller;

import com.group1.Care_Koi_System.dto.KoiFish.KoiFishRequest;
import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.entity.Enum.*;
import com.group1.Care_Koi_System.entity.Feeding;
import com.group1.Care_Koi_System.service.KoiFishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/koifish")
@CrossOrigin("**")
public class KoiFishController {
    @Autowired
    private KoiFishService koiFishService;


    @PostMapping("/create-fish/{pondID}")
    public ResponseEntity<KoiFishResponse> addKoiFish( @PathVariable int pondID, @RequestBody KoiFishRequest koiFishRequest,
                                                       @RequestParam KoiSpecies species, @RequestParam KoiGender gender,
                                                       @RequestParam KoiOrigin origin, @RequestParam HealthyStatus healthyStatus){
        return koiFishService.createKoiFish(koiFishRequest, species, gender, origin, healthyStatus, pondID);
    }
    @PutMapping("/update-fish/{pondID}/{fishID}")
    public KoiFishResponse updateKoiFish(@PathVariable int pondID, @PathVariable int fishID, @RequestBody KoiFishRequest koiFishRequest,
                                                         @RequestParam KoiSpecies species, @RequestParam KoiGender gender,
                                                         @RequestParam KoiOrigin origin, @RequestParam HealthyStatus healthyStatus){
        return  koiFishService.updateKoiFish(pondID, fishID, koiFishRequest, species,gender, origin, healthyStatus);
    }

    @DeleteMapping("/delete-fish/{koiFishID}")
    public ResponseEntity<String> deleteKoiFish(@PathVariable int koiFishID) {
        return koiFishService.deleteKoiFish(koiFishID);
    }

    @PostMapping("/calculate-food")
    public ResponseEntity<Feeding> calculateFood(
            @RequestParam int idPond,
            @RequestParam @Valid FoodType foodType)
            {

        Feeding feeding = koiFishService.calculateFood(idPond, foodType);
        return ResponseEntity.ok(feeding);
    }

    @GetMapping("/get-all-fish")
    public  ResponseEntity<?> viewAllFish(){
        return koiFishService.getAllKoiFish();
    }

    @GetMapping("/get-koi-fish-by-account")
    public ResponseEntity<?> getFishByAccount(){
        return  koiFishService.getKoiFishByAccount();
    }

    @GetMapping("/get-history/{fishID}")
    public ResponseEntity<?> getHistory(@PathVariable int fishID){
        return koiFishService.getHistory(fishID);
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAll(){
        return koiFishService.getAll();
    }
}
