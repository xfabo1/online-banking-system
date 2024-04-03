package cz.muni.fi.obs.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AccountCreateDto(

        @Schema(description = "User ID of the account owner", example = "d333c127-470b-4680-8c7c-70988998b329")
        @NotBlank(message = "User ID is required")
        String accountNumber,

        @Schema(description = "Currency code of the account", example = "CZK")
        @NotBlank(message = "Currency code is required")
        String currencyCode
) {
}
