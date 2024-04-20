package cz.muni.fi.obs.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.muni.fi.obs.data.dbo.User;
import cz.muni.fi.obs.data.enums.Nationality;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record UserDto(
        @Schema(description = "Unique ID of the user", example = "d333c127-470b-4680-8c7c-70988998b329")
        UUID id,

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

        @Schema(description = "Nationality code of the user", example = "CZ")
        Nationality nationality,

        @Schema(description = "Birth number of the user", example = "010704/4267")
        String birthNumber,

        @Schema(description = "Activity status of the user account", example = "true")
        Boolean active
) {
    public static UserDto fromUser(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName())
                      .phoneNumber(user.getPhoneNumber()).email(user.getEmail()).birthDate(user.getBirthDate())
                      .nationality(user.getNationality()).birthNumber(user.getBirthNumber()).active(user.isActive())
                      .build();
    }
}