package ru.krizhanovsky.aston.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krizhanovsky.aston.dto.AccountInfo;
import ru.krizhanovsky.aston.exception.AccountNotFoundException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.repository.AccountRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    @Transactional
    public void update(Account account) {
        getByAccountNumber(account.getAccountNumber());
        accountRepository.saveAndFlush(account);
    }

    @Override
    public Account getByAccountNumber(Long accountNumber) throws AccountNotFoundException {
        Optional<Account> account = accountRepository.findById(accountNumber);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new AccountNotFoundException(String.format("Лицевой счёт %1$s не найден", accountNumber));
        }
    }

    @Override
    public Long add(AccountInfo accountInfo) throws IncorrectPinException {
        String accountPin = accountInfo.getPin();
        if (accountPin == null || accountPin.isBlank() || accountPin.length() != 4) {
            throw new IncorrectPinException(String.format("Некорректный ПИН-код: %1$s", accountPin));
        }
        Account account = new Account();
        account.setPin(accountPin);
        account.setName(accountInfo.getName());
        return accountRepository.save(account).getAccountNumber();
    }
}
