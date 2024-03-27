package cz.muni.fi.obs.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Currency extends DomainObject {

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String code;

    private String name;

    private Set<ExchangeRate> exchangeRates = new HashSet<>();
}
