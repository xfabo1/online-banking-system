package cz.muni.fi.obs.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserUpdateDto {
        String firstName;
        String lastName;
        String phoneNumber;
        String email;
}
