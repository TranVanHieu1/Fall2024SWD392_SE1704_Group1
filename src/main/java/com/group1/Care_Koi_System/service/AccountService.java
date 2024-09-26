package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Account.LoginRequest;
import com.group1.Care_Koi_System.dto.Account.LoginResponse;
import com.group1.Care_Koi_System.dto.Account.RegisReponse;
import com.group1.Care_Koi_System.dto.Account.RegisterRequest;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Enum.AccountProviderEnum;
import com.group1.Care_Koi_System.entity.Enum.AccountRole;
import com.group1.Care_Koi_System.entity.Enum.AccountStatus;
import com.group1.Care_Koi_System.entity.Enum.GenderEnum;
import com.group1.Care_Koi_System.exceptionhandler.Account.AccountException;
import com.group1.Care_Koi_System.exceptionhandler.AuthAppException;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
            accountRepository.save(account);
            String token = jwtService.generateToken(account.getEmail());
            account.setTokens(token);
            return new RegisReponse("Registered successfully!");
        }catch (AuthAppException ex){
            ErrorCode errorCode = ex.getErrorCode();
            throw new AccountException(errorCode);
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
}
