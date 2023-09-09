package ru.krizhanovsky.aston.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService{

    private final AccountRepository accountRepository;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public void update(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account getByAccountNumber(Long accountNumber) {
        Optional<Account> account = accountRepository.findById(accountNumber);
        return account.orElse(null);
    }

    @Override
    public Long add(Account account) {
        return accountRepository.save(account).getAccountNumber();
    }
}
