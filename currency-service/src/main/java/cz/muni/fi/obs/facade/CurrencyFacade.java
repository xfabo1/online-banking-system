package cz.muni.fi.obs.facade;

import cz.muni.fi.obs.dto.CurrencyDto;
import cz.muni.fi.obs.dto.CurrencyExchangeResult;
import cz.muni.fi.obs.dto.PageRequest;
import cz.muni.fi.obs.dto.PagedResult;

import java.math.BigDecimal;

/**
 * Interface for currency related operations the module offers
 */
public interface CurrencyFacade {

    CurrencyExchangeResult exchange(String currencyFrom, String currencyTo, BigDecimal amount);

    PagedResult<CurrencyDto> listPaged(PageRequest pageRequest);
}
