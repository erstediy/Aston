package ru.krizhanovsky.aston.service;

import ru.krizhanovsky.aston.dto.AccountInfo;
import ru.krizhanovsky.aston.exception.AccountNotFoundException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.model.Account;

import java.util.List;

public interface IAccountService {

    List<Account> getAll();

    void update(Account account);

    Account getByAccountNumber(String accountNumber) throws AccountNotFoundException;

    String add(AccountInfo account) throws IncorrectPinException;

    List<Account> getAccountWithMaxBalance() throws AccountNotFoundException;
}
