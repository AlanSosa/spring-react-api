package net.alansosa.backendcursojava.exceptions;

import net.alansosa.backendcursojava.models.responses.ErrorMessage;
import net.alansosa.backendcursojava.models.responses.ValidationErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.xml.ws.Response;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
This class handles all custom exceptions
@ControllerAdvice let us do the latter.

This can be achieved using @ControllerAdvice, which is a way for Spring to have
centralized exceptions across all @RequestMapping methods through @ExceptionHandler methods and
all @RestController.

So if an exception is thrown like in the UserService CLASS. This class catches the EmailExistsException thrown
and remaps the exception to a new Exception Response object. That only contians the details we configured.
 */
@ControllerAdvice // let's us share the exception across all @Controllers.
public class AppExceptionHandler {

    @ExceptionHandler(value = EmailExistsException.class) // We tell string to handle all EmailExistsException.class that occur in the application
    public ResponseEntity<Object> handleEmailExistsException(EmailExistsException exception,
                                                             WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = PostNotRelatedException.class)
    public ResponseEntity<Object> handlePostNotRelatedException(PostNotRelatedException exception,
                                                                WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest webRequest){
        ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //This one lets us handle the @Valid errors from the models.
    @ExceptionHandler(value = MethodArgumentNotValidException.class) //Catch the exception
    public ResponseEntity<Object> handleAllExceptions(MethodArgumentNotValidException exception, WebRequest webRequest){
        Map<String, String> errors = new HashMap<>();

        //get each of the errors from the exception.
        for(ObjectError error : exception.getBindingResult().getAllErrors()){
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        }

        ValidationErrors validationErrors = new ValidationErrors(errors, new Date());

        return new ResponseEntity<>(validationErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}