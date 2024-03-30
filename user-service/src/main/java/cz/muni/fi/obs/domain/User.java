package cz.muni.fi.obs.domain;

import cz.muni.fi.obs.enums.Nationality;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User extends DomainObject {

    public User(String firstName, String lastName, String phoneNumber, String email, Date birthDate, Nationality nationality, String birthNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.birthNumber = birthNumber;
    }

    String firstName;
    String lastName;
    String phoneNumber;
    String email;
    Date birthDate;
    Nationality nationality;
    String birthNumber;
}
