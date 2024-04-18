package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "from_id")
    private Set<ExchangeRate> exchangeRatesFrom = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "to_id")
    private Set<ExchangeRate> exchangeRatesTo = new HashSet<>();
}
