package ru.krizhanovsky.aston.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krizhanovsky.aston.dto.AccountInfo;
import ru.krizhanovsky.aston.model.Account;
import ru.krizhanovsky.aston.service.IAccountService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController {
    private final IAccountService accountService;

    @PostMapping()
    public ResponseEntity<Long> addNewAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.add(account));
    }

    @GetMapping()
    public ResponseEntity<List<AccountInfo>> getAllAccounts() {
        List<Account> accounts = accountService.getAll();

        List<AccountInfo> accountsInfo = accounts.stream()
                .map(account -> new AccountInfo(account.getName(), account.getBalance()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountsInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getByAccountNumber(id));
    }
}
