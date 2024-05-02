package cz.muni.fi.obs.etl.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    String withdrawsFromAccountNumber;

    @NotBlank
    String depositsToAccountNumber;

    @Min(0)
    BigDecimal withdrawAmount;

    @Min(0)
    BigDecimal depositAmount;

    String note;

    String variableSymbol;
}
