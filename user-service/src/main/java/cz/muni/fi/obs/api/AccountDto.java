package cz.muni.fi.obs.api;

import cz.muni.fi.obs.domain.Account;
import lombok.Builder;

@Builder
public record AccountDto(String id, String accountNumber, String currencyCode) {
    public static AccountDto fromAccount(Account account) {
        if (account == null) {
            return null;
        }
        return AccountDto.builder().id(account.getId()).accountNumber(account.getAccountNumber()).currencyCode(account.getCurrencyCode()).build();
    }
}
