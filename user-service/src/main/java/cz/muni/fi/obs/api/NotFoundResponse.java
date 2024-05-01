package cz.muni.fi.obs.api;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record NotFoundResponse(

        @Schema(description = "Message describing the error",
                example = "User with id d333c127-470b-4680-8c7c-70988998b329 not found")
        String message
) {
}
