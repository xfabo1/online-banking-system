package cz.muni.fi.obs.api;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ValidationFailedResponse(

        @Schema(description = "Message describing the error", example = "Validation failed")
        String message,

        @Schema(description = "Validation errors")
        ValidationErrors validationErrors
) {
}
