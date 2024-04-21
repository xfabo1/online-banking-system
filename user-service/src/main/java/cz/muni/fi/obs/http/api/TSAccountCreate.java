package cz.muni.fi.obs.http.api;

public record TSAccountCreate(
        String customerId,
        String currencyCode,
        String accountNumber
) {
}

