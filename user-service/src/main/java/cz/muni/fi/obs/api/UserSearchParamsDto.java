package cz.muni.fi.obs.api;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.Date;

@Builder
public record UserSearchParamsDto(String firstName, String lastName, String phoneNumber, String email, Date birthDate,
                                  String birthNumber, Boolean active,


                                  Pageable pageable) {
}
