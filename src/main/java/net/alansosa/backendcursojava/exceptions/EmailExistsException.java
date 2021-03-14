package net.alansosa.backendcursojava.exceptions;

/*
This is a custom exception when an email is already existing.
* * */
public class EmailExistsException extends RuntimeException{

    private static final long serialVersionUID = 1l;

    public EmailExistsException(String message){
        super(message);
    }
}
