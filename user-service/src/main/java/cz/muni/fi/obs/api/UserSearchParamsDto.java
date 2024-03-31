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
public class UserSearchParamsDto {
        String firstName;
        String lastName;
        String phoneNumber;
        String email;
        Date birthDate;
        String birthNumber;
        boolean active;
        Integer page;
        Integer pageSize;
}
