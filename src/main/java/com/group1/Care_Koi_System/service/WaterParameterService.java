package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterRequest;
import com.group1.Care_Koi_System.dto.WaterParameter.WaterParameterResponse;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.entity.WaterParameter;
import com.group1.Care_Koi_System.exceptionhandler.Account.AccountException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.PondRepository;
import com.group1.Care_Koi_System.repository.WaterParameterRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaterParameterService {

    @Autowired
    private WaterParameterRepository waterParameterRepository;

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private AccountUtils accountUtils;

    public ResponseEntity<ResponseException> createWaterParameter(int id, WaterParameterRequest waterParameterRequest) {

        try {
            Account account = accountUtils.getCurrentAccount();
            Ponds ponds = pondRepository.findById(id);

            if (account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }
            if (ponds == null) {
                throw new SystemException(ErrorCode.POND_NOT_FOUND);
            }

            WaterParameter waterParameter = new WaterParameter();
            waterParameter.setPond(ponds);
            waterParameter.setCheckDate(LocalDateTime.now());

            // Validate and set water parameters
            validateAndSetWaterParameters(waterParameter, waterParameterRequest);

            waterParameterRepository.save(waterParameter);

            ResponseException response = new ResponseException("Save successful!");
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (SystemException ex) {
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException response = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(response, errorCode.getHttpStatus());
        }
    }

    private void validateAndSetWaterParameters(WaterParameter waterParameter, WaterParameterRequest request) {
        try {
            double percentSalt = Double.parseDouble(request.getPercentSalt());
            if (percentSalt < 0.1 || percentSalt > 0.5) {
                throw new SystemException(ErrorCode.PERCENTSALT);
            }
            waterParameter.setPercentSalt(request.getPercentSalt());

            double temperature = Double.parseDouble(request.getTemperature());
            if (temperature < 15.0 || temperature > 30.0) {
                throw new SystemException(ErrorCode.TEMPERATURE);
            }
            waterParameter.setTemperature(request.getTemperature());

            double pH = Double.parseDouble(request.getPH());
            if (pH < 7.0 || pH > 8.5) {
                throw new SystemException(ErrorCode.PH);
            }
            waterParameter.setPH(request.getPH());

            double o2 = Double.parseDouble(request.getO2());
            if (o2 < 6.0 || o2 > 8.0) {
                throw new SystemException(ErrorCode.O2);
            }
            waterParameter.setO2(request.getO2());

            double no2 = Double.parseDouble(request.getNO2());
            if (no2 > 0.20) {
                throw new SystemException(ErrorCode.NO2);
            }
            waterParameter.setNO2(request.getNO2());

            double no3 = Double.parseDouble(request.getNO3());
            if (no3 < 10 || no3 > 20) {
                throw new SystemException(ErrorCode.NO3);
            }
            waterParameter.setNO3(request.getNO3());

        } catch (NumberFormatException ex) {
            throw new SystemException(ErrorCode.INVALID_NUMBER);
        }
    }


    public ResponseEntity<ResponseException> updateWaterParameter(int id, WaterParameterRequest waterParameterRequest){
        try {
            Account account = accountUtils.getCurrentAccount();
            Ponds ponds = pondRepository.findById(id);
            if(account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }
            if (ponds == null) {
                throw new SystemException(ErrorCode.POND_NOT_FOUND);
            }
            WaterParameter waterParameter = new WaterParameter();

                try {
                    waterParameter.setPond(ponds);
                    waterParameter.setCheckDate(LocalDateTime.now());

                    validateAndSetWaterParameters(waterParameter, waterParameterRequest);
                    waterParameterRepository.save(waterParameter);
                    ResponseException response = new ResponseException("Update successful!");
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } catch (SystemException ex) {
                    throw new SystemException(ErrorCode.CAN_NOT_SAVE);
                }
            }catch (SystemException ex) {
                ErrorCode errorCode = ex.getErrorCode();
                ResponseException response = new ResponseException(ex.getMessage());
                return new ResponseEntity<>(response, errorCode.getHttpStatus());
            }
    }


    public ResponseEntity<WaterParameterResponse> getWaterParameterByPond(int id){
        try{
            Account account = accountUtils.getCurrentAccount();
            Ponds ponds = pondRepository.findById(id);
            if(account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }
            if (ponds == null) {
                throw new SystemException(ErrorCode.POND_NOT_FOUND);
            }

            List<WaterParameter> waterParameterList = ponds.getParameters();

            WaterParameterResponse waterParametes = new WaterParameterResponse();
            for(WaterParameter waterParameter : waterParameterList){
                waterParametes.setPercentSalt(waterParameter.getPercentSalt());
                waterParametes.setTemperature(waterParameter.getTemperature());
                waterParametes.setPH(waterParameter.getPH());
                waterParametes.setO2(waterParameter.getO2());
                waterParametes.setNO2(waterParameter.getNO2());
                waterParametes.setNO3(waterParameter.getNO3());
                waterParametes.setCheckDate(waterParameter.getCheckDate());
            }
            return  new ResponseEntity<>(waterParametes, HttpStatus.OK);
        }catch (SystemException ex){
            ErrorCode errorCode = ex.getErrorCode();
            WaterParameterResponse waterParameterResponse = new WaterParameterResponse(ex.getMessage());
            return  new ResponseEntity<>(waterParameterResponse, errorCode.getHttpStatus());
        }
    }

}
