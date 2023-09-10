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
import java.util.UUID;

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
    public String add(AccountInfo accountInfo) throws IncorrectPinException {
        String accountPin = accountInfo.getPin();
        if (accountPin == null || accountPin.isBlank() || accountPin.length() != 4) {
            throw new IncorrectPinException(String.format("Некорректный ПИН-код: %1$s", accountPin));
        }
        Account account = new Account(generateAccountNumber(),
                accountInfo.getName(), BCrypt.hashpw(accountPin, BCrypt.gensalt()));
        account.editBalance(accountInfo.getBalance());
        return accountRepository.save(account).getAccountNumber();
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
        UUID uuid = UUID.randomUUID();

        long mostSigBits = uuid.getMostSignificantBits();
        long leastSigBits = uuid.getLeastSignificantBits();

        long accountNumber = Math.abs(mostSigBits + leastSigBits);

        String accountNumberString = String.valueOf(accountNumber);
        if (accountNumberString.length() < 20) {
            accountNumberString = "1" + accountNumberString;
        }

        return accountNumberString;
    }
}
