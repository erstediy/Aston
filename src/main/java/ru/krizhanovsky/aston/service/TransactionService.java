package ru.krizhanovsky.aston.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.exception.AccountNotFoundException;
import ru.krizhanovsky.aston.exception.IncorrectAccountNumberException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.exception.InsufficientFundsException;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.model.Transaction;
import ru.krizhanovsky.aston.model.TransactionType;
import ru.krizhanovsky.aston.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final IAccountService accountService;

    @Override
    public void processTransaction(TransactionRequest request) throws AccountNotFoundException,
            InsufficientFundsException, IncorrectPinException, IncorrectAccountNumberException {

        Account accountFrom = accountService.getByAccountNumber(request.getAccountFromNumber());
        BigDecimal amount = request.getAmount();
        if (!request.getType().equals(TransactionType.DEPOSIT)) {
            String pin = request.getPin();

            if (pin == null || pin.isBlank()) {
                throw new IncorrectPinException("ПИН-код строго 4х значная комбинация цифр");
            }
            if (BCrypt.checkpw(pin, accountFrom.getPin())) {
                throw new IncorrectPinException(String.format("Неверный ПИН-код: %1$s", pin));
            }
            amount = amount.negate();
        }

        accountFrom.editBalance(amount);

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(amount.abs());
        transaction.setTransactionType(request.getType());
        transaction.setAccountFromNumber(accountFrom.getAccountNumber());
        if (request.getType().equals(TransactionType.TRANSFER)) {
            Account accountTo = accountService.getByAccountNumber(request.getAccountToNumber());
            transaction.setAccountToNumber(accountTo.getAccountNumber());
            accountTo.editBalance(amount.abs());
            accountService.update(accountTo);
        }
        accountService.update(accountFrom);
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        if (accountNumber.length() != 20) {
            throw new IncorrectAccountNumberException(
                    "Длина номера лицевого счёта должна быть равна 20 символам");
        }
        return transactionRepository.findAllByAccountFromNumber(accountNumber);
    }
}
