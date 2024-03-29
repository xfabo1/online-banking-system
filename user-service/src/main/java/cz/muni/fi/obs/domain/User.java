package cz.muni.fi.obs.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User extends DomainObject {

    public User(String firstName, String lastName, String phoneNumber, Date birthDate, String birthNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.birthNumber = birthNumber;
        this.email = email;
    }

    String firstName;
    String lastName;
    String phoneNumber;
    Date birthDate;
    String birthNumber;
    String email;
}
