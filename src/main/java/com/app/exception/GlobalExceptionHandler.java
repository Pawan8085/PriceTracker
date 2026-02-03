package com.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    // User exception handler
    @ExceptionHandler(UserException.class)
    public ResponseEntity<MyErrorDetails> userExceptionHandler(UserException ue, WebRequest wr) {
        MyErrorDetails err = new MyErrorDetails();
        err.setTimestap(LocalDateTime.now());
        err.setMessage(ue.getMessage());
        err.setDetails(wr.getDescription(false));

        return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    // Product exception handler
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<MyErrorDetails> productExceptionHandler(ProductException pe, WebRequest wr) {
        MyErrorDetails err = new MyErrorDetails();
        err.setTimestap(LocalDateTime.now());
        err.setMessage(pe.getMessage());
        err.setDetails(wr.getDescription(false));

        return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    // global exception handleR
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorDetails> myExceptionHandler(Exception e, WebRequest wr) {
        MyErrorDetails err = new MyErrorDetails();
        err.setTimestap(LocalDateTime.now());
        err.setMessage(e.getMessage());
        err.setDetails(wr.getDescription(false));

        return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    // NoHandlerFound Exception
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MyErrorDetails> noHandlerFoundExceptionHandler(NoHandlerFoundException nfe, WebRequest wr) {
        MyErrorDetails err = new MyErrorDetails();
        err.setTimestap(LocalDateTime.now());
        err.setMessage(nfe.getMessage());
        err.setDetails(wr.getDescription(false));

        return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }

    // Validation Exception Handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyErrorDetails> validationExceptionHandler(MethodArgumentNotValidException me,
                                                                     WebRequest wr) {
        MyErrorDetails err = new MyErrorDetails();
        err.setTimestap(LocalDateTime.now());

        // combine all error messages
        StringBuilder errorMessages = new StringBuilder();
        me.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessages.append(error.getDefaultMessage()).append("\n");
        });


        err.setMessage(errorMessages.toString());
        err.setDetails(me.getBindingResult().getFieldError().getDefaultMessage());

        return new ResponseEntity<MyErrorDetails>(err, HttpStatus.BAD_REQUEST);
    }
}
