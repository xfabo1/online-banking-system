package cz.muni.fi.obs.domain;

import cz.muni.fi.obs.enums.Nationality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class User extends Dbo {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private Nationality nationality;
    private String birthNumber;
    private Boolean active;

}
