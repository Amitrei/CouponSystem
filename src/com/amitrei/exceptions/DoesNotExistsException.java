package com.amitrei.exceptions;

public class DoesNotExistsException  extends  Exception{


    public DoesNotExistsException(String message) {
        super(message + "Does not Exists Exception");
    }
}
