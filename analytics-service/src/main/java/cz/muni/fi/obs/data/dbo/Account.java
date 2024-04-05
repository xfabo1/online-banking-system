package cz.muni.fi.obs.data.dbo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account extends Dbo{
    String accountNumber;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
