package ru.krizhanovsky.aston.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.exception.IncorrectAccountNumberException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.exception.InsufficientFundsException;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.model.Transaction;
import ru.krizhanovsky.aston.model.TransactionType;
import ru.krizhanovsky.aston.repository.TransactionRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private IAccountService accountService;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    void processTransferTransaction() {

        String senderAccountNumber = AccountService.generateAccountNumber();
        String recipientAccountNumber = AccountService.generateAccountNumber();

        Account senderAccount = Account.builder()
                .accountNumber(senderAccountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        Account recipientAccount = Account.builder()
                .accountNumber(recipientAccountNumber)
                .name("Jane Doe")
                .balance(new BigDecimal(500))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        when(accountService.getByAccountNumber(senderAccountNumber)).thenReturn(senderAccount);
        when(accountService.getByAccountNumber(recipientAccountNumber)).thenReturn(recipientAccount);

        TransactionRequest request = TransactionRequest.builder()
                .amount(new BigDecimal(200))
                .type(TransactionType.TRANSFER)
                .pin("1234")
                .accountFromNumber(senderAccountNumber)
                .accountToNumber(recipientAccountNumber)
                .build();

        transactionService.processTransaction(request);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(transactionCaptor.capture());

        Transaction savedTransaction = transactionCaptor.getValue();

        assertEquals(new BigDecimal(200), savedTransaction.getAmount());
        assertEquals(TransactionType.TRANSFER, savedTransaction.getTransactionType());
        assertEquals(senderAccountNumber, savedTransaction.getAccountFromNumber());
        assertEquals(recipientAccountNumber, savedTransaction.getAccountToNumber());


        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountService, times(2)).update(any(Account.class));

        assertEquals(new BigDecimal(800), accountService.getByAccountNumber(senderAccountNumber).getBalance());
        assertEquals(new BigDecimal(700), accountService.getByAccountNumber(recipientAccountNumber).getBalance());
    }

    @Test
    void processDepositTransaction() {

        String accountNumber = AccountService.generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        when(accountService.getByAccountNumber(accountNumber)).thenReturn(account);

        TransactionRequest request = TransactionRequest.builder()
                .amount(new BigDecimal(200))
                .type(TransactionType.DEPOSIT)
                .pin("1234")
                .accountFromNumber(accountNumber)
                .accountToNumber(null)
                .build();

        transactionService.processTransaction(request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).update(any(Account.class));

        assertEquals(new BigDecimal(1200), accountService.getByAccountNumber(accountNumber).getBalance());
    }

    @Test
    void processWithdrawalTransaction() {

        String accountNumber = AccountService.generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        when(accountService.getByAccountNumber(accountNumber)).thenReturn(account);

        TransactionRequest request = TransactionRequest.builder()
                .amount(new BigDecimal(200))
                .type(TransactionType.WITHDRAWAL)
                .pin("1234")
                .accountFromNumber(accountNumber)
                .accountToNumber(null)
                .build();

        transactionService.processTransaction(request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).update(any(Account.class));

        assertEquals(new BigDecimal(800), accountService.getByAccountNumber(accountNumber).getBalance());
    }

    @Test
    void processPaymentTransaction() {

        String accountNumber = AccountService.generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        when(accountService.getByAccountNumber(accountNumber)).thenReturn(account);

        TransactionRequest request = TransactionRequest.builder()
                .amount(new BigDecimal(200))
                .type(TransactionType.PAYMENT)
                .pin("1234")
                .accountFromNumber(accountNumber)
                .accountToNumber(null)
                .build();

        transactionService.processTransaction(request);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(accountService, times(1)).update(any(Account.class));

        assertEquals(new BigDecimal(800), accountService.getByAccountNumber(accountNumber).getBalance());
    }

    @Test
    void processTransactionWithIncorrectPin() {
        String accountNumber = AccountService.generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        when(accountService.getByAccountNumber(accountNumber)).thenReturn(account);

        TransactionRequest request = TransactionRequest.builder()
                .amount(new BigDecimal(200))
                .type(TransactionType.WITHDRAWAL)
                .pin("4321")
                .accountFromNumber(accountNumber)
                .accountToNumber(null)
                .build();

        assertThrows(IncorrectPinException.class, () -> transactionService.processTransaction(request));
    }

    @Test
    void processTransactionWithInsufficientFunds() {
        String accountNumber = AccountService.generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(100))
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        when(accountService.getByAccountNumber(accountNumber)).thenReturn(account);

        TransactionRequest request = TransactionRequest.builder()
                .amount(new BigDecimal(200))
                .type(TransactionType.WITHDRAWAL)
                .pin("1234")
                .accountFromNumber(accountNumber)
                .accountToNumber(null)
                .build();

        assertThrows(InsufficientFundsException.class, () -> transactionService.processTransaction(request));
    }

    @Test
    void getTransactionsByIncorrectAccountNumber() {
        String incorrectAccountNumber = "12345"; // Некорректный номер счета

        assertThrows(IncorrectAccountNumberException.class, () -> {
            transactionService.getTransactionsByAccountNumber(incorrectAccountNumber);
        });
    }

}