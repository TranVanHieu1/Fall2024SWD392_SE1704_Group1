package com.group1.Care_Koi_System.exceptionhandler.OrderDetail;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
