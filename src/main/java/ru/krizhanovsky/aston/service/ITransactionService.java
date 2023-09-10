package ru.krizhanovsky.aston.service;

import ru.krizhanovsky.aston.dto.TransactionRequest;
import ru.krizhanovsky.aston.model.TransactionType;

import java.math.BigDecimal;

public interface ITransactionService {

     void processTransaction(TransactionRequest request);

}
