package cz.muni.fi.obs.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountDto(
        @Schema(description = "Unique ID of the account", example = "d333c127-470b-4680-8c7c-70988998b329")
        UUID id,

        @Schema(description = "Account number", example = "12-1234567890/0100")
        String accountNumber,

        @Schema(description = "Accounts currency code", example = "CZK")
        String currencyCode
) {
}
