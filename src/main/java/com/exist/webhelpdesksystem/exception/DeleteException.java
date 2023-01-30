package com.exist.webhelpdesksystem.exception;

public abstract class DeleteException extends RuntimeException {

    public DeleteException(String message){
        super(message);
    }
}
