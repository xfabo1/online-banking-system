package cz.muni.fi.obs.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class UserCreateDto {

        @NotBlank
        String firstName;

        @NotBlank
        String lastName;

        @NotBlank
        String phoneNumber;

        @Past
        Date birthDate;

        @NotBlank
        String birthNumber;

        @Email
        String email;
}
