package com.group1.Care_Koi_System.exceptionhandler.Item;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String message) {
        super(message);
    }
}
