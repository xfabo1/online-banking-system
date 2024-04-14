package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "cs_currency")
public class Currency extends Dbo {

    public Currency(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "from")
    private Set<ExchangeRate> exchangeRatesFrom;

    @OneToMany(mappedBy = "to")
    private Set<ExchangeRate> exchangeRatesTo;
}
