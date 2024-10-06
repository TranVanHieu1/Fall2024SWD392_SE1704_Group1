package com.group1.Care_Koi_System.service;

import com.group1.Care_Koi_System.dto.Order.OrderRequest;
import com.group1.Care_Koi_System.entity.Account;
import com.group1.Care_Koi_System.entity.Enum.AccountRole;
import com.group1.Care_Koi_System.entity.Order;
import com.group1.Care_Koi_System.exceptionhandler.ErrorCode;
import com.group1.Care_Koi_System.exceptionhandler.ResponseException;
import com.group1.Care_Koi_System.exceptionhandler.SystemException;
import com.group1.Care_Koi_System.repository.OrderRepository;
import com.group1.Care_Koi_System.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    AccountUtils accountUtils;

    @Autowired
    OrderRepository orderRepository;

    public ResponseEntity<ResponseException> updateOrderStatus(int id, OrderRequest orderRequest){
        try{
            Account account = accountUtils.getCurrentAccount();
            if(account == null){
                throw new SystemException(ErrorCode.NOT_LOGIN);
            }else if(!account.getRole().equals(AccountRole.ADMIN)){
                throw new SystemException(ErrorCode.ACCOUNT_NOT_ADMIN);
            }

            Order order = orderRepository.findById(id);
            order.setStatus(orderRequest.getOrderStatus());
            orderRepository.save(order);
            ResponseException response = new ResponseException("Update successful!");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (SystemException ex){
            ErrorCode errorCode = ex.getErrorCode();
            ResponseException responseException = new ResponseException(ex.getMessage());
            return new ResponseEntity<>(responseException, errorCode.getHttpStatus());
        }
    }
}