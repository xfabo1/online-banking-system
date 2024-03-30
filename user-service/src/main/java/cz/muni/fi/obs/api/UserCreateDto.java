package cz.muni.fi.obs.api;

import cz.muni.fi.obs.validation.nationality.Nationality;
import cz.muni.fi.obs.validation.nationalityBirthNumber.NationalityBirthNumber;
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
@NationalityBirthNumber(nationalityProperty = "nationality", birthNumberProperty = "birthNumber")
public class UserCreateDto {

        @NotBlank(message = "First name is required")
        String firstName;

        @NotBlank(message = "Last name is required")
        String lastName;

        @NotBlank(message = "Phone number is required")
        String phoneNumber;

        @Email(message = "Email is not valid")
        String email;

        @Past(message = "Birth date must be in the past")
        Date birthDate;

        @Nationality
        String nationality;

        @NotBlank(message = "Birth number is required")
        String birthNumber;

}
