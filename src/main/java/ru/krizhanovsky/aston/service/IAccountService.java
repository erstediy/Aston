package ru.krizhanovsky.aston.service;

import ru.krizhanovsky.aston.model.Account;

import java.util.List;

public interface IAccountService {

    List<Account> getAll();

    void update(Account account);

    Account getByAccountNumber(Long accountNumber);

    Long add(Account account);
}
