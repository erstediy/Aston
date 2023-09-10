package ru.krizhanovsky.aston.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.exception.AccountNotFoundException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.exception.InsufficientFundsException;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.model.Transaction;
import ru.krizhanovsky.aston.model.TransactionType;
import ru.krizhanovsky.aston.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService{

    private final TransactionRepository transactionRepository;
    private final IAccountService accountService;

    @Override
    public void processTransaction(TransactionRequest request) throws AccountNotFoundException,
            InsufficientFundsException, IncorrectPinException {
        Account account = accountService.getByAccountNumber(request.getAccountNumber());
        BigDecimal amount = request.getAmount();
        if (!request.getType().equals(TransactionType.DEPOSIT)) {
            account.checkPin(request.getPin());
            amount = amount.negate();
        }
        account.editBalance(amount);
        accountService.update(account);
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount(amount);
        transaction.setTransactionType(request.getType());
        transaction.setAccountNumber(account.getAccountNumber());
        transactionRepository.save(transaction);
    }
}
