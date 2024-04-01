package cz.muni.fi.obs.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountCreateDto(@NotBlank String accountNumber,

                               @NotBlank String currencyCode) {
}
