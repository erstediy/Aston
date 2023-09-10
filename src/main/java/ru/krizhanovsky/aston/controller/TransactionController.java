package ru.krizhanovsky.aston.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.model.TransactionType;
import ru.krizhanovsky.aston.service.ITransactionService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/operations")
public class TransactionController {
    private final ITransactionService transactionService;

    @PostMapping("/process")
    public ResponseEntity<String> processTransaction(@RequestBody TransactionRequest request) {
        transactionService.processTransaction(request);
        return ResponseEntity.status(HttpStatus.OK).body("Операция успешно выполнена");
    }
}
