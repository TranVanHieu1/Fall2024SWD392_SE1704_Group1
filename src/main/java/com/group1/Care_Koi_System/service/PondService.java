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
                throw new AuthAppException(ErrorCode.INVALID_POND_NAME);
            }

            Ponds existingPond = pondRepository.findByNamePond(request.getNamePond());
            if (existingPond != null) {
                throw new AuthAppException(ErrorCode.POND_ALREADY_EXISTS);
            }

            Ponds pond = new Ponds();
            pond.setNamePond(request.getNamePond());
            pond.setImage(request.getImage());
            pond.setSize(request.getSize());
            pond.setHeight(request.getHeight());
            double volume = request.getSize() * request.getHeight();
            pond.setVolume(volume);
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
            if(existingPond == null) {
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
            double volume = request.getSize() * request.getHeight();
            existingPond.setVolume(volume);
            existingPond.setCreateAt(LocalDateTime.now());

            pondRepository.save(existingPond);

            ResponseException responseException = new ResponseException("Update Successfully!");
            return ResponseEntity.ok(responseException);
        }catch (SystemException ex) {
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

            if(pondsList.isEmpty()){
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<ViewPondResponse> ponds = new ArrayList<>();
            for(Ponds pond: pondsList){


                List<Pond_KoiFish> pondKoiFish = pond_koiFishRepository.findKoiFishByPondsId(pond.getId());
                List<String> fishName = pondKoiFish.stream()
                        .map(koiFish -> koiFish.getKoiFish().getFishName()).toList();
                ponds.add(new ViewPondResponse(
                        pond.getId(),
                        pond.getNamePond(),
                        fishName,
                        pond.getImage(),
                        pond.getSize(),
                        pond.getVolume()
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

            if(pondsList.isEmpty()){
                throw new SystemException(ErrorCode.EMPTY);
            }
            List<ViewPondResponse> ponds = new ArrayList<>();
            for(Ponds pond: pondsList){

                List<Pond_KoiFish> pondKoiFish = pond_koiFishRepository.findKoiFishByPondsId(pond.getId());
                List<String> fishName = pondKoiFish.stream()
                        .map(koiFish -> koiFish.getKoiFish().getFishName()).toList();
                ponds.add(new ViewPondResponse(
                        pond.getId(),
                        pond.getNamePond(),
                        fishName,
                        pond.getImage(),
                        pond.getSize(),
                        pond.getVolume()
                ));

            }
            return new ResponseEntity<>(ponds, HttpStatus.OK);
        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }

    public PondSearchResponse searchPond(String namePond, Integer id) {
        PondSearchResponse response = new PondSearchResponse();


        List<Ponds> ponds = pondRepository.searchByNamePondAndIdPond(namePond, id);

        if (ponds.isEmpty()) {
            response.setMessage("No ponds found!");
            response.setPondsList(Collections.emptyList());
        } else {
            List<PondResponse> pondResponses = ponds.stream()
                    .map(pond -> {
                        PondResponse pondResponse = new PondResponse();
                        pondResponse.setNamePond(pond.getNamePond());
                        pondResponse.setId(pond.getId());
                        pondResponse.setSize(pond.getSize());
                        pondResponse.setVolume(pond.getVolume());
                        pondResponse.setImage(pond.getImage());
                        pondResponse.setCreateAt(pond.getCreateAt());

                        List<KoiFishResponse> koiFishResponses = pond.getKoiFishList().stream()
                                .map(pondKoiFish -> {
                                    KoiFishResponse koiFishResponse = new KoiFishResponse();
                                    koiFishResponse.setId(pondKoiFish.getKoiFish().getId());
                                    koiFishResponse.setFishName(pondKoiFish.getKoiFish().getFishName());
                                    koiFishResponse.setSize(pondKoiFish.getKoiFish().getSize());
                                    koiFishResponse.setWeigh(pondKoiFish.getKoiFish().getWeigh());
                                    koiFishResponse.setDateAdded(pondKoiFish.getDateAdded());
                                    return koiFishResponse;
                                })
                                .collect(Collectors.toList());

                        pondResponse.setKoiFishList(koiFishResponses);

                        List<WaterParameterResponse> waterParameterResponses = pond.getParameters().stream()
                                .map(parameters -> {
                                    WaterParameterResponse wpResponse = new WaterParameterResponse();
                                    wpResponse.setPercentSalt(parameters.getPercentSalt());
                                    wpResponse.setTemperature(parameters.getTemperature());
                                    wpResponse.setPH(parameters.getPH());
                                    wpResponse.setO2(parameters.getO2());
                                    wpResponse.setNO2(parameters.getNO2());
                                    wpResponse.setNO3(parameters.getNO3());
                                    wpResponse.setCheckDate(parameters.getCheckDate());
                                    return wpResponse;
                                })
                                .collect(Collectors.toList());

                        pondResponse.setParameters(waterParameterResponses);

                        return pondResponse;
                    })
                    .collect(Collectors.toList());

            response.setMessage("Ponds retrieved successfully.");
            response.setPondsList(pondResponses);
        }

        return response;
    }
}



