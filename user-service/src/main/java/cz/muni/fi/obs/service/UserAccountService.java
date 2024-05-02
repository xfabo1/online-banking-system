package cz.muni.fi.obs.service;

import cz.muni.fi.obs.api.AccountCreateDto;
import cz.muni.fi.obs.api.AccountDto;
import cz.muni.fi.obs.exceptions.ResourceNotFoundException;
import cz.muni.fi.obs.http.TransactionServiceClient;
import cz.muni.fi.obs.http.api.TSAccount;
import cz.muni.fi.obs.http.api.TSAccountCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserAccountService {

    private final TransactionServiceClient transactionServiceClient;

    @Autowired
    public UserAccountService(TransactionServiceClient transactionServiceClient) {
        this.transactionServiceClient = transactionServiceClient;

    }

    public AccountDto create(UUID userId, AccountCreateDto accountCreateDto) {
        TSAccountCreate tsAccountCreate = new TSAccountCreate(
                userId.toString(),
                accountCreateDto.currencyCode(),
                accountCreateDto.accountNumber()
        );
        TSAccount tsAccount = transactionServiceClient.createAccount(tsAccountCreate);
        return new AccountDto(
                UUID.fromString(tsAccount.id()),
                tsAccount.accountNumber(),
                tsAccount.currencyCode()
        );
    }

    public List<AccountDto> getUserAccounts(UUID userId) {
        try {
            List<TSAccount> tsAccounts = transactionServiceClient.getAccountsByCustomerId(userId.toString());
            return tsAccounts.stream()
                             .map(tsAccount -> new AccountDto(
                                     UUID.fromString(tsAccount.id()),
                                     tsAccount.accountNumber(),
                                     tsAccount.currencyCode()
                             ))
                             .collect(Collectors.toList());
        } catch (ResourceNotFoundException e) {
            return Collections.emptyList();
        }
    }
}
