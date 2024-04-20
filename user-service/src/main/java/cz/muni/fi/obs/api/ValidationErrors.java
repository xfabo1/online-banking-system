package cz.muni.fi.obs.api;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
public record ValidationErrors(
        @Schema(description = "List of global errors", example = "[\"Invalid birth number for given nationality\"]")
        List<String> globalErrors,

        @Schema(description = "Map of field errors", example = "{\"firstName\":\"firstName is required\"}")
        Map<String, String> fieldErrors
) {

        public static ValidationErrors fromBindException(org.springframework.validation.BindException ex) {
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
