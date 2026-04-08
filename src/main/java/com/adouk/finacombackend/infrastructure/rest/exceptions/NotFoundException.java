package com.adouk.finacombackend.infrastructure.rest.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }
}