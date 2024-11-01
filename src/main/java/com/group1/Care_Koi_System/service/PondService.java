package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.ApiRes;
import com.group1.Care_Koi_System.dto.KoiFish.KoiFishResponse;
import com.group1.Care_Koi_System.dto.Pond.PondRequest;
import com.group1.Care_Koi_System.dto.Pond.PondResponse;
import com.group1.Care_Koi_System.dto.Pond.ViewPondResponse;
import com.group1.Care_Koi_System.dto.SearchPond.PondSearchResponse;
import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterResponse;
import com.group1.Care_Koi_System.entity.*;
import com.group1.Care_Koi_System.exceptionhandler.AuthAppException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.KoiFishRepository;
import com.group1.Care_Koi_System.repository.PondRepository;
import com.group1.Care_Koi_System.repository.Pond_KoiFishRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
public class PondService {

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountUtils accountUtils;

    @Autowired
    private Pond_KoiFishRepository pond_koiFishRepository;


    public ResponseEntity<ResponseException> createPond(PondRequest request) {
        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            if (request.getNamePond() == null || request.getNamePond().trim().isEmpty()) {
                throw new SystemException(ErrorCode.INVALID_POND_NAME);
            }

            Ponds existingPond = pondRepository.findByNamePond(request.getNamePond());
            if (existingPond != null) {
                throw new SystemException(ErrorCode.POND_ALREADY_EXISTS);
            }

            if(request.getSize() < 2){
                throw new SystemException(ErrorCode.INVALID_SIZE);
            }
            if(request.getHeight() < 0.8){
                throw new SystemException(ErrorCode.INVALID_HEIGHT);
            }

            Ponds pond = new Ponds();
            pond.setAccount(account);
            pond.setNamePond(request.getNamePond());
            pond.setImage(request.getImage());
            pond.setSize(request.getSize());
            pond.setHeight(request.getHeight());
            double volume = 0;
            volume = request.getSize() * request.getHeight();
            pond.setVolume(volume);
            int maximum = 0;
            maximum = (int) volume * 2;
            pond.setMaximum((maximum));
            pond.setCreateAt(LocalDateTime.now());


            pondRepository.save(pond);

            ResponseException responseException = new ResponseException("Create Pond Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(responseException);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }


    public ResponseEntity<ResponseException> updatePond(int pondId, PondRequest request) {
        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            Ponds existingPond = pondRepository.findById(pondId);
            if (existingPond == null) {
                throw new SystemException(ErrorCode.POND_NOT_FOUND);
            }

            if (request.getNamePond() == null || request.getNamePond().isEmpty()) {
                throw new AuthAppException(ErrorCode.INVALID_POND_NAME);
            }

            Ponds pondWithSameName = pondRepository.findByNamePond(request.getNamePond());
            if (pondWithSameName != null && pondWithSameName.getId() != pondId) {
                throw new AuthAppException(ErrorCode.POND_ALREADY_EXISTS);
            }


            existingPond.setNamePond(request.getNamePond());
            existingPond.setImage(request.getImage());
            existingPond.setSize(request.getSize());
            existingPond.setHeight(request.getHeight());
            double volume = 0;
            volume = request.getSize() * request.getHeight();
            existingPond.setVolume(volume);
            int maximum = 0;
            maximum = (int) volume * 2;
            existingPond.setMaximum((maximum));
            existingPond.setCreateAt(LocalDateTime.now());

            pondRepository.save(existingPond);

            ResponseException responseException = new ResponseException("Update Successfully!");
            return ResponseEntity.ok(responseException);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<ResponseException> deletePond(int id) {
        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            try {
                Ponds pond = pondRepository.findById(id);

                pond.setDeleted(true);
                pondRepository.save(pond);

                ResponseException responseException = new ResponseException("Delete successful!");
                return new ResponseEntity<>(responseException, HttpStatus.OK);
            } catch (SystemException ex) {
                throw new SystemException(ErrorCode.CAN_NOT_DELETE);
            }

        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getPondsByAccount() {
        try {
            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            List<Ponds> pondsList = pondRepository.findByAccount(account).stream()
                    .filter(pond -> !pond.isDeleted()).toList();

            if (pondsList.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<ViewPondResponse> ponds = new ArrayList<>();
            for (Ponds pond : pondsList) {


                List<Pond_KoiFish> pondKoiFish = pond_koiFishRepository.findKoiFishByPondsId(pond.getId());
                List<String> fishName = pondKoiFish.stream()
                        .filter(koiFish -> !koiFish.getKoiFish().isDeleted()) // Filter non-deleted fish
                        .map(koiFish -> koiFish.getKoiFish().getFishName())   // Map to fish names
                        .toList();
                ponds.add(new ViewPondResponse(
                        pond.getId(),
                        pond.getNamePond(),
                        fishName,
                        pond.getImage(),
                        pond.getSize(),
                        pond.getVolume(),
                        pond.getHeight()
                ));

            }
            return new ResponseEntity<>(ponds, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public ResponseEntity<?> getAllPonds() {
        try {

            List<Ponds> pondsList = pondRepository.findAll().stream()
                    .filter(pond -> !pond.isDeleted()).toList();

            if (pondsList.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<ViewPondResponse> ponds = new ArrayList<>();
            for (Ponds pond : pondsList) {

                List<Pond_KoiFish> pondKoiFish = pond_koiFishRepository.findKoiFishByPondsId(pond.getId());
                List<String> fishName = pondKoiFish.stream()
                        .filter(koiFish -> !koiFish.getKoiFish().isDeleted()) // Filter non-deleted fish
                        .map(koiFish -> koiFish.getKoiFish().getFishName())   // Map to fish names
                        .toList();
                ponds.add(new ViewPondResponse(
                        pond.getId(),
                        pond.getNamePond(),
                        fishName,
                        pond.getImage(),
                        pond.getSize(),
                        pond.getVolume(),
                        pond.getHeight()
                ));

            }
            return new ResponseEntity<>(ponds, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }


    public ResponseEntity<?> getPondByID(int pondID) {
        try {

            Account account = accountUtils.getCurrentAccount();
            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            Ponds pond = pondRepository.findById(pondID);

            if (pond == null) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            List<Pond_KoiFish> pondKoiFish = pond_koiFishRepository.findKoiFishByPondsId(pond.getId());

            List<WaterParameter> waterParameter = pond.getParameters();

            if (waterParameter == null || waterParameter.isEmpty()) {
                throw new SystemException(ErrorCode.EMPTY);
            }

            List<String> fishName = pondKoiFish.stream()
                    .filter(koiFish -> !koiFish.getKoiFish().isDeleted()) // Filter non-deleted fish
                    .map(koiFish -> koiFish.getKoiFish().getFishName())   // Map to fish names
                    .toList();
            ViewPondResponse viewPondResponse = new ViewPondResponse(
                    pond.getNamePond(),
                    fishName,
                    pond.getSize(),
                    pond.getVolume(),
                    waterParameter.get(0).getPercentSalt(),
                    waterParameter.get(0).getTemperature(),
                    waterParameter.get(0).getO2(),
                    waterParameter.get(0).getNO2(),
                    waterParameter.get(0).getNO3(),
                    waterParameter.get(0).getPH()
            );
            return new ResponseEntity<>(viewPondResponse, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }
}



