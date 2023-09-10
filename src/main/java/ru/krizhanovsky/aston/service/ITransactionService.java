package ru.krizhanovsky.aston.service;

import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.model.Transaction;
import ru.krizhanovsky.aston.model.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {

     void processTransaction(TransactionRequest request);

     List<Transaction> getTransactionsByAccountNumber(String accountNumber);

}
