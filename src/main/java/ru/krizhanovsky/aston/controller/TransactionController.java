package ru.krizhanovsky.aston.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.service.ITransactionService;


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
}
