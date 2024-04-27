package cz.muni.fi.obs.etl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {

    private String id;

    private String customerId;

    private String currencyCode;

    private String accountNumber;
}
