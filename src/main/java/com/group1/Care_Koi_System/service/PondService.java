package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Pond.PondRequest;
import com.group1.Care_Koi_System.entity.Ponds;
import com.group1.Care_Koi_System.exceptionhandler.AuthAppException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.repository.PondRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PondService {

    private final PondRepository pondRepository;
    private final AccountService accountService;
    private final AccountUtils accountUtils;

    @Autowired
    public PondService(PondRepository pondRepository, AccountService accountService, AccountUtils accountUtils) {
        this.pondRepository = pondRepository;
        this.accountService = accountService;
        this.accountUtils = accountUtils;
    }

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
}



