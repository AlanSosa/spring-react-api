package net.alansosa.backendcursojava.exceptions;

public class PostNotRelatedException extends RuntimeException{

    private static final long serialVersionUID = 1l;

    public PostNotRelatedException(String message){
        super(message);
    }
}
