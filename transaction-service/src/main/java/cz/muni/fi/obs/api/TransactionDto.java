package cz.muni.fi.obs.api;

import java.math.BigDecimal;

import cz.muni.fi.obs.data.dbo.TransactionDbo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
    @NotBlank
    String withdrawsFromAccountId;

    @NotBlank
    String depositsToAccountId;

    @Min(0)
    BigDecimal withdrawAmount;

    @Min(0)
    BigDecimal depositAmount;

    String note;

    String variableSymbol;

    public static TransactionDto fromDbo(TransactionDbo transactionDbo) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setNote(transactionDbo.getNote());
        transactionDto.setDepositAmount(transactionDbo.getDepositAmount());
        transactionDto.setVariableSymbol(transactionDbo.getVariableSymbol());
        transactionDto.setWithdrawAmount(transactionDbo.getWithdrawAmount());
        transactionDto.setWithdrawsFromAccountId(transactionDbo.getWithdrawsFrom().getId());
        transactionDto.setDepositsToAccountId(transactionDbo.getDepositsTo().getId());
        return transactionDto;
    }
}
