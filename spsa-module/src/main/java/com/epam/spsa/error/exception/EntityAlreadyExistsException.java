package com.epam.spsa.error.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message){
        super(message);
    }

}
