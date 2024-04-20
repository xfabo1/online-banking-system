package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.data.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    @Test
    void create_accountCreated_returnsAccount() {
        AccountCreateDto accountCreateDto = new AccountCreateDto("1234567890", "Joe's Account");
        AccountDto account = new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account");
        when(userAccountRepository.create(any())).thenReturn(account);

        AccountDto response = userAccountService.create(UUID.randomUUID(), accountCreateDto);

        verify(userAccountRepository).create(any());
        assertThat(response).isEqualTo(account);
    }

    @Test
    void getUserAccounts_accountsFound_returnsAccounts() {
        UUID userId = UUID.randomUUID();

        List<AccountDto> accounts = Arrays.asList(
                new AccountDto(UUID.randomUUID(), "1234567890", "Joe's Account"),
                new AccountDto(UUID.randomUUID(), "0987654321", "Joe's Other Account")
        );
        when(userAccountRepository.findByUserId(userId)).thenReturn(accounts);

        List<AccountDto> response = userAccountService.getUserAccounts(userId);

        verify(userAccountRepository).findByUserId(userId);
        assertThat(response).isEqualTo(accounts);
    }
}
