package cz.muni.fi.obs.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserUpdateDto(

        @Schema(description = "First name of the user", example = "John")
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        String lastName,

        @Schema(description = "Phone number of the user", example = "+420 123 456 789")
        String phoneNumber,

        @Schema(description = "Email of the user", example = "john.doe@example.com")
        String email
) {
}
