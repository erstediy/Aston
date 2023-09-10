package ru.krizhanovsky.aston.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.model.Transaction;
import ru.krizhanovsky.aston.service.ITransactionService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/operations")
@Api(tags = "Bank account transactions API")
public class TransactionController {
    private final ITransactionService transactionService;

    @PostMapping("/process")
    @ApiOperation("Process transaction on the bank account")
    public ResponseEntity<String> processTransaction(@RequestBody TransactionRequest request) {
        transactionService.processTransaction(request);
        return ResponseEntity.status(HttpStatus.OK).body("Операция успешно выполнена");
    }

    @GetMapping("/{accountNumber}")
    @ApiOperation("Get all transactions for account by account number")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountNumber(accountNumber));
    }
}
