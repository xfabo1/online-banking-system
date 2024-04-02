package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class UserControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFoundExceptions(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleValidationExceptions(BindException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Validation failed");
        response.put("errors", getValidationErrors(ex));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getValidationErrors(BindException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        List<String> globalErrors = new ArrayList<>();
        for (ObjectError error : ex.getGlobalErrors()) {
            globalErrors.add(error.getDefaultMessage());
        }

        Map<String, Object> errors = new HashMap<>();
        errors.put("fieldErrors", fieldErrors);
        errors.put("globalErrors", globalErrors);

        return errors;
    }
}