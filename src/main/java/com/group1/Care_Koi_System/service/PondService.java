package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Pond.PondRequest;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.exceptionhandler.AuthAppException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.PondRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PondService {

    @Autowired
    private PondRepository pondRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private  AccountUtils accountUtils;


    public Ponds createPond(PondRequest request, int accountId) {

        if (request.getNamePond() == null || request.getNamePond().trim().isEmpty()) {
            throw new AuthAppException(ErrorCode.INVALID_POND_NAME);
        }

        Ponds existingPond = pondRepository.findByNamePondAndAccountId(request.getNamePond(), accountId);
        if (existingPond != null) {
            throw new AuthAppException(ErrorCode.POND_ALREADY_EXISTS);
        }

        Ponds pond = new Ponds();
        pond.setNamePond(request.getNamePond());
        pond.setImage(request.getImage());
        pond.setSize(request.getSize());
        pond.setVolume(request.getVolume());
        pond.setCreateAt(LocalDateTime.now());
        pond.setAccount(accountUtils.getCurrentAccount());

        return pondRepository.save(pond);
    }


    public Ponds updatePond(int pondId, PondRequest request, int accountId) {

        Ponds existingPond = pondRepository.findByIdAndAccountId(pondId, accountId)
                .orElseThrow(() -> new AuthAppException(ErrorCode.POND_NOT_FOUND));

        if (request.getNamePond() == null || request.getNamePond().isEmpty()) {
            throw new AuthAppException(ErrorCode.INVALID_POND_NAME);
        }

        Ponds pondWithSameName = pondRepository.findByNamePondAndAccountId(request.getNamePond(), accountId);
        if (pondWithSameName != null && pondWithSameName.getId() != pondId) {
            throw new AuthAppException(ErrorCode.POND_ALREADY_EXISTS);
        }

        existingPond.setNamePond(request.getNamePond());
        existingPond.setImage(request.getImage());
        existingPond.setSize(request.getSize());
        existingPond.setVolume(request.getVolume());
        existingPond.setCreateAt(LocalDateTime.now());

        return pondRepository.save(existingPond);
    }

    public ResponseEntity<ResponseException> deletePond(int id){
        try{
            Account account =  accountUtils.getCurrentAccount();
            if(account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }

            try{
                Ponds pond = pondRepository.findById(id);

                pond.setDeleted(true);
                pondRepository.save(pond);

                ResponseException responseException = new ResponseException("Delete successful!");
                return  new ResponseEntity<>(responseException, HttpStatus.OK);
            }catch (SystemException ex){
                throw new SystemException(ErrorCode.CAN_NOT_DELETE);
            }

        }catch (SystemException ex){
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException respon = new ResponseException(ex.getMessage());
            return  new ResponseEntity<>(respon, errorCode.getHttpStatus());
        }
    }
}



