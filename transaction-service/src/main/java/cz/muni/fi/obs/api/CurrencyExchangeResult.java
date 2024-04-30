package cz.muni.fi.obs.api;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CurrencyExchangeResult(String symbolFrom, String symbolTo, Double exchangeRate, BigDecimal sourceAmount, BigDecimal destAmount) {}
