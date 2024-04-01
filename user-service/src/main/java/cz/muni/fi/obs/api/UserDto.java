package cz.muni.fi.obs.api;

import cz.muni.fi.obs.domain.User;
import cz.muni.fi.obs.enums.Nationality;
import lombok.Builder;

import java.util.Date;

@Builder
public record UserDto(String id, String firstName, String lastName, String phoneNumber, String email, Date birthDate,
                      Nationality nationality, String birthNumber, Boolean active) {
    public static UserDto fromUser(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder().id(user.getId()).firstName(user.getFirstName()).lastName(user.getLastName()).phoneNumber(user.getPhoneNumber()).email(user.getEmail()).birthDate(user.getBirthDate()).nationality(user.getNationality()).birthNumber(user.getBirthNumber()).active(user.getActive()).build();
    }
}
