package ru.krizhanovsky.aston.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "Bank accounts API")
public class AccountController {
    private final IAccountService accountService;

    @PostMapping()
    @ApiOperation("Create new bank account")
    public ResponseEntity<String> addNewAccount(@RequestBody AccountInfo account) {
        return ResponseEntity.ok(accountService.add(account));
    }

    @GetMapping()
    @ApiOperation("Get all bank accounts")
    public ResponseEntity<List<AccountInfo>> getAllAccounts() {
        List<Account> accounts = accountService.getAll();

        List<AccountInfo> accountsInfo = getAccountInfos(accounts);

        return ResponseEntity.ok(accountsInfo);
    }

    @GetMapping("/{accountNumber}")
    @ApiOperation("Get bank account by account number")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getByAccountNumber(accountNumber));
    }

    @GetMapping("/maxBalance")
    @ApiOperation("Get bank account with max balance")
    public ResponseEntity<List<AccountInfo>> getAccountWithMaxBalance() {
        List<Account> accounts = accountService.getAccountWithMaxBalance();

        List<AccountInfo> accountsInfo = getAccountInfos(accounts);

        return ResponseEntity.ok(accountsInfo);
    }

    private List<AccountInfo> getAccountInfos(List<Account> accounts) {
        return accounts.stream()
                .map(account -> new AccountInfo(account.getName(), account.getBalance(), null))
                .collect(Collectors.toList());
    }
}
