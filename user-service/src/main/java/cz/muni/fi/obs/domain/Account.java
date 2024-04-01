package cz.muni.fi.obs.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account extends DomainObject {

    String userId;
    String accountNumber;
    String currencyCode;

    public Account(String userId, String accountNumber, String currencyCode) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.currencyCode = currencyCode;
    }
}
