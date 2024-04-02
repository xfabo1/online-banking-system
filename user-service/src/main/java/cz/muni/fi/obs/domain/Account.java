package cz.muni.fi.obs.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Account extends Dbo {

    private String userId;
    private String accountNumber;
    private String currencyCode;

}
