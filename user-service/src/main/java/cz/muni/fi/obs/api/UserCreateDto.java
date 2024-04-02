package cz.muni.fi.obs.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.muni.fi.obs.validation.nationality.Nationality;
import cz.muni.fi.obs.validation.nationalityBirthNumber.NationalityBirthNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@NationalityBirthNumber(nationalityProperty = "nationality", birthNumberProperty = "birthNumber")
public record UserCreateDto(

        @Schema(description = "First name of the user", example = "John")
        @NotBlank(message = "First name is required")
        String firstName,

        @Schema(description = "Last name of the user", example = "Doe")
        @NotBlank(message = "Last name is required") String lastName,

        @Schema(description = "Phone number of the user", example = "+420 123 456 789")
        @NotBlank(message = "Phone number is required") String phoneNumber,

        @Schema(description = "Email of the user", example = "john.doe@example.com")
        @Email(message = "Email is not valid") String email,

        @Schema(description = "Birth date of the user", example = "1990-01-01", format = "date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Past(message = "Birth date must be in the past") LocalDate birthDate,

        @Schema(description = "Nationality code of the user", example = "CZ")
        @Nationality(message = "Nationality is not valid") String nationality,

        @Schema(description = "Birth number of the user, corresponding with the nationality", example = "010704/4267")
        @NotBlank(message = "Birth number is required") String birthNumber) {
}
