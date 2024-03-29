package cz.muni.fi.obs.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AccountCreateDto {
        @NotBlank
        String accountNumber;

        @NotBlank
        String currencyCode;
}
