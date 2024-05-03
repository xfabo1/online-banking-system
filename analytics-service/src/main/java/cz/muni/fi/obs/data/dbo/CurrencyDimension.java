package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency_dim")
public class CurrencyDimension extends Dbo {
    @Column(nullable = false, unique = true)
    String symbol;
}
