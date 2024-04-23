package cz.muni.fi.obs.data.dbo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cs_exchange_rate")
public class ExchangeRate extends Dbo {

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant validUntil;

    @ManyToOne
    private Currency from;

    @ManyToOne
    private Currency to;

    @Column(nullable = false)
    private double conversionRate;
}
