package com.group1.Care_Koi_System.exceptionhandler;

import com.group1.Care_Koi_System.exceptionhandler.OrderDetail.ItemNotFoundException;
import com.group1.Care_Koi_System.exceptionhandler.OrderDetail.OrderDetailNotFoundException;
import com.group1.Care_Koi_System.exceptionhandler.OrderDetail.OutOfStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderDetailNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderDetailNotFoundException(OrderDetailNotFoundException ex) {
        Map<String, String> response = Map.of("message", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFoundException(ItemNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<String> handleOutOfStockException(OutOfStockException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
