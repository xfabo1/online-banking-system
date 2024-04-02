package cz.muni.fi.obs.dto;

import java.math.BigDecimal;

public record CurrencyExchangeRequest(String from, String to, BigDecimal amount) {
}
