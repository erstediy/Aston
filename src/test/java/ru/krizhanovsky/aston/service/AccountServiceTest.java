package ru.krizhanovsky.aston.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.krizhanovsky.aston.dto.AccountInfo;
import ru.krizhanovsky.aston.exception.AccountNotFoundException;
import ru.krizhanovsky.aston.exception.IncorrectAccountNumberException;
import ru.krizhanovsky.aston.exception.IncorrectPinException;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.krizhanovsky.aston.service.AccountService.generateAccountNumber;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void update() {
        Account accountToUpdate = Account.builder()
                .accountNumber(generateAccountNumber())
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .id(UUID.randomUUID())
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt())).build();

        accountRepository.save(accountToUpdate);

        when(accountRepository.findByAccountNumber(anyString())).thenReturn(java.util.Optional.of(accountToUpdate));

        accountToUpdate.setName("Jane Doe");

        accountService.update(accountToUpdate);

        verify(accountRepository, times(1)).saveAndFlush(accountToUpdate);

        Account updatedAccount = accountService.getByAccountNumber(accountToUpdate.getAccountNumber());
        assertEquals("Jane Doe", updatedAccount.getName());
    }

    @Test
    void updateWithInvalidAccountNumber() {
        Account accountToUpdate = Account.builder()
                .accountNumber("1234")
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .id(UUID.randomUUID())
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        try {
            accountService.update(accountToUpdate);
            fail("Должно быть выброшено исключение IncorrectAccountNumberException");
        } catch (IncorrectAccountNumberException e) {
            assertTrue(true);
        } catch (AccountNotFoundException e) {
            fail("AccountNotFoundException не должно быть выброшено");
        }
    }

    @Test
    void updateWithNonExistentAccount() {
        String nonExistentAccountNumber = generateAccountNumber();

        Account accountToUpdate = Account.builder()
                .accountNumber(nonExistentAccountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .id(UUID.randomUUID())
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        try {
            accountService.update(accountToUpdate);
            fail("Должно быть выброшено исключение AccountNotFoundException");
        } catch (AccountNotFoundException e) {
            assertTrue(true);
        } catch (IncorrectAccountNumberException e) {
            fail("IncorrectAccountNumberException не должно быть выброшено");
        }
    }

    @Test
    void getByAccountNumber() {
        String accountNumber = generateAccountNumber();

        Account accountToRetrieve = Account.builder()
                .accountNumber(accountNumber)
                .name("Jhon Doe")
                .balance(new BigDecimal(1000))
                .id(UUID.randomUUID())
                .pin(BCrypt.hashpw("1234", BCrypt.gensalt()))
                .build();

        accountRepository.save(accountToRetrieve);

        when(accountRepository.findByAccountNumber(accountToRetrieve.getAccountNumber()))
                .thenReturn(Optional.of(accountToRetrieve));
        Account retrievedAccount = accountService.getByAccountNumber(accountNumber);

        assertEquals(accountToRetrieve, retrievedAccount);
    }

    @Test
    void add() {
        AccountInfo accountInfo = AccountInfo.builder()
                .name("Jane Doe")
                .balance(new BigDecimal(1000))
                .pin("1234")
                .build();

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> {
            Account savedAccount = invocation.getArgument(0);
            return Account.builder().accountNumber(savedAccount.getAccountNumber())
                    .name(savedAccount.getName()).balance(savedAccount.getBalance())
                    .pin(savedAccount.getPin()).build();
        });

        Account account = accountService.add(accountInfo);

        assertNotNull(account);

        when(accountRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        Account retrievedAccount = accountService.getByAccountNumber(account.getAccountNumber());
        assertNotNull(retrievedAccount);
        assertEquals(account.getAccountNumber(), retrievedAccount.getAccountNumber());
    }


    @Test
    void add_WithIncorrectPin() {
        AccountInfo accountInfo = AccountInfo.builder()
                .name("Jane Doe")
                .balance(new BigDecimal(1000))
                .pin("12345")
                .build();

        assertThrows(IncorrectPinException.class, () -> accountService.add(accountInfo));
    }
}