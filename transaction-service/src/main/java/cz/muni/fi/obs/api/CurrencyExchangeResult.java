package cz.muni.fi.obs.api;


import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record CurrencyExchangeResult(String symbolFrom, String symbolTo, Double exchangeRate, BigDecimal sourceAmount, BigDecimal destAmount) {}
