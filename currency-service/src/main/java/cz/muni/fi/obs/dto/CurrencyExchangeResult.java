package cz.muni.fi.obs.dto;


import java.math.BigDecimal;

public record CurrencyExchangeResult(String symbolFrom, String symbolTo, Double exchangedAt, BigDecimal sourceAmount, BigDecimal destAmount) {}