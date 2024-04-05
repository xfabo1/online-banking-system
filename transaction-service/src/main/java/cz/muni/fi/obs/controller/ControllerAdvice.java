package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.exceptions.NotFoundResponse;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.exceptions.ValidationErrors;
import cz.muni.fi.obs.exceptions.ValidationFailedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<NotFoundResponse> handleNotFoundExceptions(ResourceNotFoundException ex) {
        NotFoundResponse response = NotFoundResponse.builder()
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationFailedResponse> handleValidationExceptions(BindException ex) {
        ValidationFailedResponse response = ValidationFailedResponse.builder()
                .message("Validation failed")
                .validationErrors(getValidationErrors(ex))
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationFailedResponse> handleNonReadableExceptions(HttpMessageNotReadableException ex) {
        ValidationFailedResponse response = ValidationFailedResponse.builder()
                .message("Validation failed")
                .validationErrors(ValidationErrors.builder().globalErrors(List.of(ex.getMessage())).build())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private ValidationErrors getValidationErrors(BindException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        List<String> globalErrors = new ArrayList<>();
        for (ObjectError error : ex.getGlobalErrors()) {
            globalErrors.add(error.getDefaultMessage());
        }

        return ValidationErrors.builder().fieldErrors(fieldErrors).globalErrors(globalErrors).build();
    }
}
