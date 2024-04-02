package cz.muni.fi.obs.api;

import cz.muni.fi.obs.domain.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AccountDto(
        @Schema(description = "Unique ID of the account", example = "d333c127-470b-4680-8c7c-70988998b329")
        String id,

        @Schema(description = "Account number", example = "12-1234567890/0100")
        String accountNumber,

        @Schema(description = "Accounts currency code", example = "CZK")
        String currencyCode
) {
    public static AccountDto fromAccount(Account account) {
        if (account == null) {
            return null;
        }
        return AccountDto.builder().id(account.getId()).accountNumber(account.getAccountNumber()).currencyCode(account.getCurrencyCode()).build();
    }
}
