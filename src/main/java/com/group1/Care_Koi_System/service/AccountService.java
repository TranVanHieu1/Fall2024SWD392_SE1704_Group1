package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Account.*;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Enum.AccountProviderEnum;
import com.group1.Care_Koi_System.entity.Enum.AccountRole;
import com.group1.Care_Koi_System.entity.Enum.AccountStatus;
import com.group1.Care_Koi_System.entity.Enum.GenderEnum;
import com.group1.Care_Koi_System.exceptionhandler.Account.AccountException;
import com.group1.Care_Koi_System.exceptionhandler.AuthAppException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.AccountRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountUtils accountUtils;

    public Account getAccountByEmail(String email){
        Optional<Account> account = accountRepository.findByEmail(email);
        return account.orElse(null);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = accountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userDetails;
    }


    public RegisReponse registerAccount(RegisterRequest registerRequest){
        try{
            Account tempAccount = getAccountByEmail(registerRequest.getEmail());

            if(tempAccount != null){
                if(tempAccount.getStatus().equals(AccountStatus.VERIFIED)){
                    throw new AccountException(ErrorCode.EMAIL_EXISTED);
                }else if(tempAccount.getStatus().equals(AccountStatus.UNVERIFIED)){
                    throw new AccountException(ErrorCode.EMAIL_WAIT_VERIFY);
                }
            }

            Account account = new Account();
            account.setEmail(registerRequest.getEmail());
            account.setPassWord(passwordEncoder.encode(registerRequest.getPassword()));
            account.setRole(AccountRole.MEMBER);
            account.setStatus(AccountStatus.VERIFIED);
            account.setCreateAt(LocalDateTime.now());
            account.setProvider(AccountProviderEnum.LOCAL);
            account.setDeleted(false);
            accountRepository.save(account);
            String token = jwtService.generateToken(account.getEmail());
            account.setTokens(token);
            return new RegisReponse("Registered successfully!");
        }catch (AuthAppException ex){
            throw new AccountException(ex.getErrorCode());
        }

    }


    public ResponseEntity<LoginResponse> checkLogin(LoginRequest loginRequest) {
        try {
            // GET EMAIL BY REQUEST DTO AND VALIDATION EMAIL
            Account account = getAccountByEmail(loginRequest.getEmail());

            if (account == null) {
                throw new AuthAppException(ErrorCode.EMAIL_NOT_FOUND);
            }
            if (account.getStatus().equals(AccountStatus.UNVERIFIED)) {
                throw new AuthAppException(ErrorCode.ACCOUNT_NOT_VERIFY);
            }
            Authentication authentication = null;
            try {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );
            } catch (Exception e) {
                throw new AuthAppException(ErrorCode.USERNAME_PASSWORD_NOT_CORRECT);
            }


            Account returnAccount = (Account) authentication.getPrincipal();
            // CALL FUNC || GENERATE TOKEN (1DAY) AND REFRESH TOKEN (7DAYS)
            account.setTokens(jwtService.generateToken(account.getEmail()));
            account.setRefreshToken(jwtService.generateRefreshToken(account.getEmail()));

            String responseString = "Login successful";
            LoginResponse loginResponseDTO = new LoginResponse(
                    responseString,
                    returnAccount.getTokens(),
                    returnAccount.getRefreshToken()
            );
            return new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);


        } catch (AuthAppException e) {
            ErrorCode errorCode = e.getErrorCode();
            String errorResponse = "Login failed";
            LoginResponse loginResponseDTO = new LoginResponse(
                    e.getMessage(),
                    null,
                    null
            );
            return new ResponseEntity<>(loginResponseDTO, errorCode.getHttpStatus());
        }
    }


    //get all account
    public ResponseEntity<List<AccountResponse>> getAllAccount(){
        try{
            Account account =  accountUtils.getCurrentAccount();
            if(account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            } else if (!account.getRole().equals(AccountRole.ADMIN)) {
                throw new SystemException(ErrorCode.ACCOUNT_NOT_ADMIN);
            }
            List<Account> accounts = accountRepository.findAll().stream()
                    .filter(acc -> !acc.isDeleted()).toList();

            List<AccountResponse> accountList = new ArrayList<>();

            for(Account acc : accounts){
                accountList.add(new AccountResponse(
                        acc.getId(),
                        acc.getUsername(),
                        acc.getEmail(),
                        acc.getAddress(),
                        acc.getPhone(),
                        acc.getRole()
                ));
            }

            if(accountList.isEmpty()){
                throw new SystemException(ErrorCode.ACCOUNT_NOT_FOUND);
            }
            return new ResponseEntity<>(accountList, HttpStatus.OK);

        }catch (SystemException ex){
            List<AccountResponse> emptyList = new ArrayList<>();

            // Đặt mã lỗi và thông báo lỗi vào headers
            HttpHeaders headers = new HttpHeaders();
            headers.add("errorCode", ex.getErrorCode().toString());
            headers.add("message", ex.getErrorCode().getMessage());

            // Trả về danh sách trống cùng với mã lỗi trong headers
            return new ResponseEntity<>(emptyList, headers, HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<ResponseException> deleteAccount(int id){
        try{
            Account account =  accountUtils.getCurrentAccount();
            if(account == null) {
                throw new SystemException(ErrorCode.NOT_LOGIN);
            } else if (!account.getRole().equals(AccountRole.ADMIN)) {
                throw new SystemException(ErrorCode.ACCOUNT_NOT_ADMIN);
            }

            try{

                Account acc = accountRepository.findById(id);
                if(acc == null){
                    throw new SystemException(ErrorCode.ACCOUNT_NOT_FOUND);
                }
                acc.setDeleted(true);
                accountRepository.save(acc);
                ResponseException exception = new ResponseException("Delete Successful!");
                return new ResponseEntity<>(exception, HttpStatus.OK);
            }catch (SystemException ex){
                throw new SystemException(ErrorCode.CAN_NOT_DELETE);
            }
        }catch (SystemException ex){
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException responseException = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(responseException, errorCode.getHttpStatus());
        }
    }
}
