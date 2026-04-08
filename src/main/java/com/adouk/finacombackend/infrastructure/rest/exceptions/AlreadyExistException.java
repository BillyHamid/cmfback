package com.adouk.finacombackend.infrastructure.rest.exceptions;

public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String message){
        super(message);
    }
}