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

@Service
public class WaterParameterService {

    @Autowired
    private WaterParameterRepository waterParameterRepository;

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private AccountUtils accountUtils;

    public ResponseEntity<ResponseException> createWaterParameter(int id, WaterParameterRequest waterParameterRequest){

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
              try{
                  waterParameter.setPond(ponds);
                  waterParameter.setCheckDate(LocalDateTime.now());
                  // Kiểm tra phần trăm muối
                  double percentSalt = Double.parseDouble(waterParameterRequest.getPercentSalt());
                  if (percentSalt < 0.1 || percentSalt > 0.5) {
                      throw new SystemException(ErrorCode.PERCENTSALT);
                  }
                  waterParameter.setPercentSalt(waterParameterRequest.getPercentSalt());

                  // Kiểm tra nhiệt độ
                  double temperature = Double.parseDouble(waterParameterRequest.getTemperature());
                  if (temperature < 15.0 || temperature > 30.0) { // Điều chỉnh theo khoảng nhiệt độ phù hợp
                      throw new SystemException(ErrorCode.TEMPERATURE);
                  }
                  waterParameter.setTemperature(waterParameterRequest.getTemperature());

                  // Kiểm tra NO₂
                  double no2 = Double.parseDouble(waterParameterRequest.getNO2());
                  if (no2 > 0.20) {
                      throw new SystemException(ErrorCode.NO2);
                  }
                  waterParameter.setNO2(waterParameterRequest.getNO2());

                  // Kiểm tra NO₃
                  double no3 = Double.parseDouble(waterParameterRequest.getNO3());
                  if (no3 < 10 || no3 > 20) {
                      throw new SystemException(ErrorCode.NO3);
                  }
                  waterParameter.setNO3(waterParameterRequest.getNO3());

                  // Kiểm tra O₂
                  double o2 = Double.parseDouble(waterParameterRequest.getO2());
                  if (o2 < 6.0 || o2 > 8.0) { // Điều chỉnh theo khoảng O₂ phù hợp
                      throw new SystemException(ErrorCode.O2);
                  }
                  waterParameter.setO2(waterParameterRequest.getO2());

                  // Kiểm tra pH
                  double pH = Double.parseDouble(waterParameterRequest.getPH());
                  if (pH < 7.0 || pH > 8.5) {
                      throw new SystemException(ErrorCode.PH);
                  }
                  waterParameter.setPH(waterParameterRequest.getPH());


              }catch (SystemException ex){
                  ErrorCode errorCode = ex.getErrorCode();
                  ResponseException response = new ResponseException(ex.getMessage());
                  return new ResponseEntity<>(response, errorCode.getHttpStatus());
              }

              waterParameterRepository.save(waterParameter);
              ResponseException response = new ResponseException("Save successful!");
              return new ResponseEntity<>(response, HttpStatus.OK);
          } catch (SystemException ex) {
              throw new SystemException(ErrorCode.CAN_NOT_SAVE);
          }
      }catch (SystemException ex){
          ErrorCode errorCode = ex.getErrorCode();
          ResponseException response = new ResponseException(ex.getMessage());
          return new ResponseEntity<>(response, errorCode.getHttpStatus());
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
                try{
                    waterParameter.setPond(ponds);
                    waterParameter.setCheckDate(LocalDateTime.now());
                    // Kiểm tra phần trăm muối
                    double percentSalt = Double.parseDouble(waterParameterRequest.getPercentSalt());
                    if (percentSalt < 0.1 || percentSalt > 0.5) {
                        throw new SystemException(ErrorCode.PERCENTSALT);
                    }
                    waterParameter.setPercentSalt(waterParameterRequest.getPercentSalt());

                    // Kiểm tra nhiệt độ
                    double temperature = Double.parseDouble(waterParameterRequest.getTemperature());
                    if (temperature < 15.0 || temperature > 30.0) { // Điều chỉnh theo khoảng nhiệt độ phù hợp
                        throw new SystemException(ErrorCode.TEMPERATURE);
                    }
                    waterParameter.setTemperature(waterParameterRequest.getTemperature());

                    // Kiểm tra NO₂
                    double no2 = Double.parseDouble(waterParameterRequest.getNO2());
                    if (no2 > 0.20) {
                        throw new SystemException(ErrorCode.NO2);
                    }
                    waterParameter.setNO2(waterParameterRequest.getNO2());

                    // Kiểm tra NO₃
                    double no3 = Double.parseDouble(waterParameterRequest.getNO3());
                    if (no3 < 10 || no3 > 20) {
                        throw new SystemException(ErrorCode.NO3);
                    }
                    waterParameter.setNO3(waterParameterRequest.getNO3());

                    // Kiểm tra O₂
                    double o2 = Double.parseDouble(waterParameterRequest.getO2());
                    if (o2 < 6.0 || o2 > 8.0) { // Điều chỉnh theo khoảng O₂ phù hợp
                        throw new SystemException(ErrorCode.O2);
                    }
                    waterParameter.setO2(waterParameterRequest.getO2());

                    // Kiểm tra pH
                    double pH = Double.parseDouble(waterParameterRequest.getPH());
                    if (pH < 7.0 || pH > 8.5) {
                        throw new SystemException(ErrorCode.PH);
                    }
                    waterParameter.setPH(waterParameterRequest.getPH());


                }catch (SystemException ex){
                    ErrorCode errorCode = ex.getErrorCode();
                    ResponseException response = new ResponseException(ex.getMessage());
                    return new ResponseEntity<>(response, errorCode.getHttpStatus());
                }

                waterParameterRepository.save(waterParameter);
                ResponseException response = new ResponseException("Update successful!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (SystemException ex) {
                throw new SystemException(ErrorCode.CAN_NOT_SAVE);
            }
        }catch (SystemException ex){
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException response = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(response, errorCode.getHttpStatus());
        }
    }

}
