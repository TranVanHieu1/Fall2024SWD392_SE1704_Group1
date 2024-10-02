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
              waterParameter.setPond(ponds);
              waterParameter.setCheckDate(LocalDateTime.now());
              waterParameter.setPercentSalt(waterParameterRequest.getPercentSalt());
              waterParameter.setTemperature(waterParameterRequest.getTemperature());
              waterParameter.setPH(waterParameterRequest.getPH());
              waterParameter.setO2(waterParameterRequest.getO2());
              waterParameter.setNO2(waterParameterRequest.getNO2());
              waterParameter.setNO3(waterParameterRequest.getNO3());
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
}
