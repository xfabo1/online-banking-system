package cz.muni.fi.obs.api;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ErrorResponse(

        @Schema(description = "Message describing the error")
        String message
) {
}
