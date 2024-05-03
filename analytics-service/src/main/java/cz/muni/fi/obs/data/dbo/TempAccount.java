package cz.muni.fi.obs.data.dbo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TempAccount {
    private String id;

    private String customerId;

    private String currencyCode;

    private String accountNumber;
}
