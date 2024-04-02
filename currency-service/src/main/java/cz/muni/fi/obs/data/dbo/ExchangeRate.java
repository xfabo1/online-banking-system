package cz.muni.fi.obs.data.dbo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ExchangeRate extends Dbo {

    private Instant createdAt;

    private Instant validUntil;

    private Currency from;

    private Currency to;

    private double conversionRate;
}
