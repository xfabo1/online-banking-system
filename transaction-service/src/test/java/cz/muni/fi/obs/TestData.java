package cz.muni.fi.obs;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import cz.muni.fi.obs.api.TransactionCreateDto;
import cz.muni.fi.obs.data.dbo.AccountDbo;
import cz.muni.fi.obs.data.dbo.TransactionDbo;
import cz.muni.fi.obs.data.dbo.TransactionState;

public class TestData {

	public static final String accountId = "account-id";

	public static final List<TransactionDbo> withdrawTransactions = Arrays.asList(
			TransactionDbo.builder()
					.id("1")
					.depositsTo(depositAccount())
					.conversionRate(0.25)
					.note("note")
					.withdrawAmount(BigDecimal.valueOf(1000))
					.withdrawsFrom(withdrawAccount())
					.depositAmount(BigDecimal.valueOf(250))
					.variableSymbol("123")
					.transactionState(TransactionState.SUCCESSFUL)
					.build(),
			TransactionDbo.builder()
					.id("2")
					.depositsTo(depositAccount())
					.conversionRate(2.0)
					.note("note")
					.withdrawAmount(BigDecimal.valueOf(2))
					.withdrawsFrom(withdrawAccount())
					.depositAmount(BigDecimal.valueOf(2))
					.variableSymbol("123")
					.transactionState(TransactionState.SUCCESSFUL)
					.build()
	);

	public static final List<TransactionDbo> depositTransactions = Arrays.asList(
			TransactionDbo.builder()
					.id("3")
					.depositsTo(depositAccount())
					.conversionRate(3.0)
					.note("note")
					.withdrawAmount(BigDecimal.valueOf(1000.5))
					.withdrawsFrom(withdrawAccount())
					.depositAmount(BigDecimal.valueOf(3001.5))
					.variableSymbol("123")
					.transactionState(TransactionState.SUCCESSFUL)
					.build(),

			TransactionDbo.builder()
					.id("4")
					.depositsTo(depositAccount())
					.conversionRate(11.0)
					.note("note")
					.withdrawAmount(BigDecimal.valueOf(4))
					.withdrawsFrom(withdrawAccount())
					.depositAmount(BigDecimal.valueOf(44))
					.variableSymbol("123")
					.transactionState(TransactionState.SUCCESSFUL)
					.build()
	);

	public static TransactionCreateDto transactionCreateDto() {
		return new TransactionCreateDto(
				TestData.withdrawTransactions.getFirst().getWithdrawsFrom().getId(),
				TestData.withdrawTransactions.getFirst().getDepositsTo().getId(),
				TestData.withdrawTransactions.getFirst().getWithdrawAmount(),
				TestData.withdrawTransactions.getFirst().getNote(),
				TestData.withdrawTransactions.getFirst().getVariableSymbol()
		);
	}

	public static AccountDbo withdrawAccount() {
		return AccountDbo.builder()
				.id(accountId)
				.currencyCode("CZK")
				.build();
	}

	public static AccountDbo depositAccount() {
		return AccountDbo.builder()
				.id("test")
				.currencyCode("CZK")
				.build();
	}
}
