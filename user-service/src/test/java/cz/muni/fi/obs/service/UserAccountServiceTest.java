package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.http.TransactionServiceClient;
import cz.muni.fi.obs.http.api.TSAccount;
import cz.muni.fi.obs.http.api.TSAccountCreate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private TransactionServiceClient transactionServiceClient;

    @InjectMocks
    private UserAccountService userAccountService;

    @Test
    void create_accountCreated_returnsAccount() {
        UUID userId = UUID.randomUUID();
        AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
        TSAccountCreate tsAccountCreate = new TSAccountCreate(userId.toString(),
                                                              accountCreateDto.currencyCode(),
                                                              accountCreateDto.accountNumber()
        );
        TSAccount tsAccount = new TSAccount(UUID.randomUUID().toString(),
                                            userId.toString(),
                                            accountCreateDto.currencyCode(),
                                            false
        );
        AccountDto account = new AccountDto(UUID.fromString(tsAccount.id()),
                                            userId,
                                            tsAccount.currencyCode(),
                                            false
        );
        when(transactionServiceClient.createAccount(tsAccountCreate)).thenReturn(tsAccount);

        AccountDto response = userAccountService.create(userId, accountCreateDto);

        verify(transactionServiceClient).createAccount(tsAccountCreate);
        assertThat(response).isEqualTo(account);
    }

    @Test
    void getUserAccounts_accountsFound_returnsAccounts() {
        UUID userId = UUID.randomUUID();

        List<AccountDto> accounts = Arrays.asList(
                new AccountDto(UUID.randomUUID(), userId, "Joe's Account", false),
                new AccountDto(UUID.randomUUID(), userId, "Joe's Other Account", false)
        );
        List<TSAccount> tsAccounts = accounts.stream()
                                             .map(account -> new TSAccount(account.id().toString(),
                                                                           userId.toString(),
                                                                           account.currencyCode(),
                                                                           false
                                             ))
                                             .collect(Collectors.toList());
        when(transactionServiceClient.getAccountsByCustomerId(userId.toString())).thenReturn(tsAccounts);

        List<AccountDto> response = userAccountService.getUserAccounts(userId);

        verify(transactionServiceClient).getAccountsByCustomerId(userId.toString());
        assertThat(response).isEqualTo(accounts);
    }
}
