package com.group1.Care_Koi_System.exceptionhandler;

public class InvalidToken extends RuntimeException{
    public InvalidToken(String message) {
        super(message);
    }
}
