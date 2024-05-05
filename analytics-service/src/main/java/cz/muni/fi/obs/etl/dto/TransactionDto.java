package cz.muni.fi.obs.etl.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionDto {
    @NotBlank
    String withdrawsFromAccountId;

    @NotBlank
    String depositsToAccountId;

    @NotNull
    @Min(0)
    BigDecimal withdrawAmount;

    @NotNull
    @Min(0)
    BigDecimal depositAmount;

    String note;

    String variableSymbol;
}
