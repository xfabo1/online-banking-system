package cz.muni.fi.obs.exception;

import cz.muni.fi.obs.domain.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NoExchangeRate extends RuntimeException {

    private final Currency currencyFrom;
    private final Currency currencyTo;

    public NoExchangeRate(Currency currencyFrom, Currency currencyTo) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
    }

    @Override
    public String getMessage() {
        return "No exchange rate from: " + currencyFrom.getCode() + " to: " + currencyTo.getCode() + ".";
    }
}
