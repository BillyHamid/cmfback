package com.adouk.finacombackend.infrastructure.rest.exceptions;

public class BusinessException extends RuntimeException{
    public BusinessException(String message){
        super(message);
    }
}