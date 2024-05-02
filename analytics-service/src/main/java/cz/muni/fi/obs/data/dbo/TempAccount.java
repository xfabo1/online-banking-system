package cz.muni.fi.obs.data.dbo;

import cz.muni.fi.obs.etl.dto.AccountDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * TODO: add fields see {@link AccountDto}, entity is just used for temporary storage for ETL
 */

@Getter
@NoArgsConstructor
public class TempAccount {
    private String id;

    private String customerId;

    private String currencyCode;

    private String accountNumber;
}
