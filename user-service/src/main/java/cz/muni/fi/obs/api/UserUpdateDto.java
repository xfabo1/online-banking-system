package cz.muni.fi.obs.api;

import cz.muni.fi.obs.validation.someOptionalPresent.SomeOptionalPresent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Optional;

@Builder
@SomeOptionalPresent
public record UserUpdateDto(

        @Schema(description = "First name of the user", example = "John")
        Optional<String> firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        Optional<String> lastName,

        @Schema(description = "Phone number of the user", example = "+420702336584")
        Optional<String> phoneNumber,

        @Schema(description = "Email of the user", example = "someEmail@new.com")
        Optional<String> email) {
}
