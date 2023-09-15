package ru.krizhanovsky.aston.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.krizhanovsky.aston.dto.AccountInfo;
import ru.krizhanovsky.aston.exception.AccountNotFoundException;
import ru.krizhanovsky.aston.exception.IncorrectAccountNumberException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.repository.AccountRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public void update(Account account) throws AccountNotFoundException,
            IncorrectAccountNumberException {
        getByAccountNumber(account.getAccountNumber());
        accountRepository.saveAndFlush(account);
    }

    @Override
    public Account getByAccountNumber(String accountNumber) throws AccountNotFoundException,
            IncorrectAccountNumberException {
        if (accountNumber == null || accountNumber.length() != 20) {
            throw new IncorrectAccountNumberException(
                    "Длина номера лицевого счёта должна быть равна 20 символам");
        }
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        return account.orElseThrow(() ->
                new AccountNotFoundException(String.format("Лицевой счёт %1$s не найден", accountNumber)));

    }

    @Override
    public Account add(AccountInfo accountInfo) throws IncorrectPinException {
        String accountPin = accountInfo.getPin();
        if (accountPin == null || accountPin.isBlank() || accountPin.length() != 4) {
            throw new IncorrectPinException(String.format("Некорректный ПИН-код: %1$s", accountPin));
        }
        Account account = Account.builder().accountNumber(generateAccountNumber())
                .name(accountInfo.getName()).pin(BCrypt.hashpw(accountPin, BCrypt.gensalt())).build();
        account.editBalance(accountInfo.getBalance());
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccountWithMaxBalance() throws AccountNotFoundException {
        List<Account> accountsWithMaxBalance = accountRepository.findWithMaxBalance();
        if(accountsWithMaxBalance.isEmpty()) {
            throw new AccountNotFoundException("В реестре нет ни одного лицевого счёта, " +
                    "либо все счета пустые");
        }
        return accountsWithMaxBalance;
    }

    /**
     * Генератор случайного номера лицевого счёта
     *
     * @return номер лицевого счёта
     */
    public static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < 20; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }
}
