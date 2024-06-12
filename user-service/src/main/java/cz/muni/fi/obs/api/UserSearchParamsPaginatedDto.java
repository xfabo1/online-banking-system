package cz.muni.fi.obs.api;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;


@Builder
public record UserSearchParamsPaginatedDto(
        Optional<String> firstName,
        Optional<String> lastName,
        Optional<String> phoneNumber,
        Optional<String> email,
        Optional<LocalDate> birthDate,
        Optional<String> birthNumber,
        Optional<Boolean> active,
        Pageable pageable
) {
    public UserSearchParamsPaginatedDto(
            UserSearchParamsDto userSearchParamsDto,
            Pageable pageable
    ) {
        this(
                userSearchParamsDto.firstName(),
                userSearchParamsDto.lastName(),
                userSearchParamsDto.phoneNumber(),
                userSearchParamsDto.email(),
                userSearchParamsDto.birthDate(),
                userSearchParamsDto.birthNumber(),
                userSearchParamsDto.active(),
                pageable
        );
    }
}

