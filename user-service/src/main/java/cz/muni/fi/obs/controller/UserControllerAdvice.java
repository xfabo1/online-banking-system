package cz.muni.fi.obs.controller;

import cz.muni.fi.obs.api.ErrorResponse;
import cz.muni.fi.obs.api.NotFoundResponse;
import cz.muni.fi.obs.api.ValidationErrors;
import cz.muni.fi.obs.api.ValidationFailedResponse;
import cz.muni.fi.obs.exceptions.ExternalServiceException;
import cz.muni.fi.obs.exceptions.UserNotFoundException;
import feign.FeignException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<NotFoundResponse> handleNotFoundExceptions(UserNotFoundException ex) {
        NotFoundResponse response = NotFoundResponse.builder()
                                                    .message(ex.getMessage())
                                                    .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationFailedResponse> handleValidationExceptions(BindException ex) {
        ValidationFailedResponse response = ValidationFailedResponse.builder()
                                                                    .message("Validation failed")
                                                                    .validationErrors(ValidationErrors.fromBindException(
                                                                            ex))
                                                                    .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationFailedResponse> handleNonReadableExceptions() {
        ValidationFailedResponse response = ValidationFailedResponse.builder()
                                                                    .message("Validation failed")
                                                                    .validationErrors(
                                                                            ValidationErrors.builder()
                                                                                            .globalErrors(List.of(
                                                                                                    "Http message not" +
                                                                                                            " readable"))
                                                                                            .build()
                                                                    )
                                                                    .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ValidationFailedResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getParameter().getParameterName();
        if (fieldName == null) {
            fieldName = "Unknown";
        }

        ValidationFailedResponse response = ValidationFailedResponse.builder()
                                                                    .message("Validation failed")
                                                                    .validationErrors(
                                                                            ValidationErrors.builder()
                                                                                            .fieldErrors(Map.of(
                                                                                                    fieldName,
                                                                                                    "Invalid format"
                                                                                            ))
                                                                                            .build()
                                                                    )
                                                                    .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ValidationFailedResponse> handlePSQLException(PSQLException ex) {
        String fieldName = extractFieldName(ex.getMessage());

        if (fieldName != null) {
            ValidationFailedResponse response = ValidationFailedResponse.builder()
                                                                        .message("Validation failed")
                                                                        .validationErrors(
                                                                                ValidationErrors.builder()
                                                                                                .fieldErrors(Map.of(
                                                                                                        fieldName,
                                                                                                        "Already used" +
                                                                                                                " by " +
                                                                                                                "another user - must be unique"
                                                                                                ))
                                                                                                .build()
                                                                        )
                                                                        .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            throw new InternalError("Unexpected error occurred.");
        }
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ErrorResponse> handleClientConnectionExceptions(ExternalServiceException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    public ResponseEntity<String> handleBadRequestExceptions(FeignException.BadRequest ex) {
        return new ResponseEntity<>(ex.contentUTF8(), HttpStatus.BAD_REQUEST);
    }


    private String extractFieldName(String message) {
        String fieldName = null;
        if (message.contains("Key (")) {
            int start = message.indexOf("Key (") + 5;
            int end = message.indexOf(")");
            fieldName = message.substring(start, end);
        }

        // change snake_case to camelCase
        if (fieldName != null) {
            String[] parts = fieldName.split("_");
            StringBuilder camelCase = new StringBuilder(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                camelCase.append(parts[i].substring(0, 1).toUpperCase()).append(parts[i].substring(1));
            }
            fieldName = camelCase.toString();
        }

        return fieldName;
    }
}