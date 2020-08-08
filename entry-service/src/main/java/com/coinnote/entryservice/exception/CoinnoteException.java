package com.coinnote.entryservice.exception;

public class CoinnoteException extends RuntimeException{
    public CoinnoteException(String message){
        super(message);
    }

    public CoinnoteException(String id, String classType){
        super("Element with id = "+id+" of type " +classType+ " not found");
    }
}
