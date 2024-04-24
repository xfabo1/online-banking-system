package cz.muni.fi.obs.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AccountCreateDto(

        @Schema(description = "Account number", example = "19-2000145399/0800")
        @NotBlank(message = "Account number is required")
        String accountNumber,

        @Schema(description = "Currency code of the account", example = "CZK")
        @NotBlank(message = "Currency code is required")
        String currencyCode
) {
}
