package cz.muni.fi.obs.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Builder
public record UserSearchParamsDto(

        @Schema(description = "First name of the user", example = "John")
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        String lastName,

        @Schema(description = "Phone number of the user", example = "+420 123 456 789")
        String phoneNumber,

        @Schema(description = "Email of the user", example = "john.doe@example.com")
        String email,

        @Schema(description = "Birth date of the user", example = "1990-01-01", format = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate,

        @Schema(description = "Birth number of the user", example = "010704/4267")
        String birthNumber,

        @Schema(description = "Activity status of the user account", example = "true")
        Boolean active,

        @Schema(description = "Pageable object for pagination")
        Pageable pageable
) {
}
