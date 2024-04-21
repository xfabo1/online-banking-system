package cz.muni.fi.obs.http.api;

public record TSAccount(
        String id,
        String customerId,
        String currencyCode,
        String accountNumber
) {
}
