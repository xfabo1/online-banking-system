package cz.muni.fi.obs.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Optional;


@Builder
public record UserSearchParamsDto(

        @Schema(description = "First name of the user", example = "John")
        Optional<String> firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        Optional<String> lastName,

        @Schema(description = "Phone number of the user", example = "+420123456789")
        Optional<String> phoneNumber,

        @Schema(description = "Email of the user", example = "john.doe@example.com")
        Optional<String> email,

        @Schema(description = "Birth date of the user", example = "1990-01-01", format = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        Optional<LocalDate> birthDate,

        @Schema(description = "Birth number of the user", example = "010704/4267")
        Optional<String> birthNumber,

        @Schema(description = "Activity status of the user account", defaultValue = "true")
        Optional<Boolean> active
) {
}

