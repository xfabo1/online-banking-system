package cz.muni.fi.obs.dto;


import java.math.BigDecimal;

public record CurrencyExchangeResult(String symbolFrom, String symbolTo, Double exchangeRate, BigDecimal sourceAmount, BigDecimal destAmount) {}