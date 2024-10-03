package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.KoiFish.KoiFishRequest;
import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.KoiFish;
import com.group1.Care_Koi_System.entity.Pond_KoiFish;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.exceptionhandler.Account.AccountException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.KoiFish.KoiFishException;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.repository.KoiFishRepository;
import com.group1.Care_Koi_System.repository.PondRepository;
import com.group1.Care_Koi_System.repository.Pond_KoiFishRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class KoiFishService {

    @Autowired
    private KoiFishRepository koiFishRepository;

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private Pond_KoiFishRepository pond_koiFishRepository;

    public ResponseEntity<KoiFishResponse> createKoiFish(KoiFishRequest koiFishRequest, int pondID) {
        Account account;
        try {
            account = accountUtils.getCurrentAccount();
        } catch (Exception ex) {
            throw new AccountException(ErrorCode.NOT_LOGIN);
        }

        try {

            //save koi fish
            KoiFish koiFish = new KoiFish();
            Pond_KoiFish pondKoiFish = new Pond_KoiFish();
            koiFish.setFishName(koiFishRequest.getFishName());
            koiFish.setImageFish(koiFishRequest.getImageFish());
            koiFish.setAge(koiFishRequest.getAge());
            koiFish.setSpecies(koiFishRequest.getSpecies());
            koiFish.setSize(koiFishRequest.getSize());
            koiFish.setWeigh(koiFishRequest.getWeigh());
            koiFish.setGender(koiFishRequest.getGender());
            koiFish.setOrigin(koiFishRequest.getOrigin());
            koiFish.setHealthyStatus(koiFishRequest.getHealthyStatus());
            koiFish.setNote(koiFishRequest.getNote());
            koiFishRepository.save(koiFish);
            //find ponds by id
            Ponds ponds = pondRepository.findById(pondID);
            //save pond_koifish

            pondKoiFish.setPonds(ponds);
            pondKoiFish.setKoiFish(koiFish);
            pondKoiFish.setDateAdded(LocalDateTime.now());
            pond_koiFishRepository.save(pondKoiFish);


            // Create the response object
            KoiFishResponse response = new KoiFishResponse();
            response.setPondID(koiFish.getId());
            response.setFishName(koiFish.getFishName());
            response.setImageFish(koiFish.getImageFish());
            response.setAge(koiFish.getAge());
            response.setSpecies(koiFish.getSpecies());
            response.setSize(koiFish.getSize());
            response.setWeigh(koiFish.getWeigh());
            response.setGender(koiFish.getGender());
            response.setOrigin(koiFish.getOrigin());
            response.setHealthyStatus(koiFish.getHealthyStatus());
            response.setNote(koiFish.getNote());
            response.setDateAdded(pondKoiFish.getDateAdded());

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ErrorCode errorCode = ErrorCode.CAN_NOT_SAVE;
            KoiFishResponse exceptionResponse = new KoiFishResponse(errorCode.getMessage());
            return new ResponseEntity<>(exceptionResponse, errorCode.getHttpStatus());
        }
    }

    public KoiFishResponse updateKoiFish(int id, KoiFishRequest koiFishRequest) {
        Optional<KoiFish> optionalKoiFish = koiFishRepository.findById(id);
        if (optionalKoiFish.isEmpty()) {
            throw new RuntimeException("KoiFish with ID " + id + " not found.");
        }
        KoiFish koiFish = optionalKoiFish.get();
        // Cập nhật thông tin từ KoiFishRequest vào KoiFish
        koiFish.setFishName(koiFishRequest.getFishName());
        koiFish.setImageFish(koiFishRequest.getImageFish());
        koiFish.setAge(koiFishRequest.getAge());
        koiFish.setSpecies(koiFishRequest.getSpecies());
        koiFish.setSize(koiFishRequest.getSize());
        koiFish.setWeigh(koiFishRequest.getWeigh());
        koiFish.setGender(koiFishRequest.getGender());
        koiFish.setOrigin(koiFishRequest.getOrigin());
        koiFish.setHealthyStatus(koiFishRequest.getHealthyStatus());
        koiFish.setNote(koiFishRequest.getNote());
        // Lưu lại KoiFish đã cập nhật
        koiFishRepository.save(koiFish);
        return convertToResponse(koiFish);
    }

    private KoiFishResponse convertToResponse(KoiFish koiFish) {
        KoiFishResponse response = new KoiFishResponse();
        response.setId(koiFish.getId());
        response.setFishName(koiFish.getFishName());
        response.setImageFish(koiFish.getImageFish());
        response.setAge(koiFish.getAge());
        response.setSpecies(koiFish.getSpecies());
        response.setSize(koiFish.getSize());
        response.setWeigh(koiFish.getWeigh());
        response.setGender(koiFish.getGender());
        response.setOrigin(koiFish.getOrigin());
        response.setHealthyStatus(koiFish.getHealthyStatus());
        response.setNote(koiFish.getNote());
        response.setPondID(koiFish.getPondKoiFish().get(0).getPonds().getId());
        response.setDateAdded(koiFish.getPondKoiFish().get(0).getDateAdded());
        return response;
    }
}
