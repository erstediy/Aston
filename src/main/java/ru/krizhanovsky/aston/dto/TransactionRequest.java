package ru.krizhanovsky.aston.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.krizhanovsky.aston.model.TransactionType;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class TransactionRequest {
    private BigDecimal amount;
    private TransactionType type;
    private String pin;
    private String accountFromNumber;
    private String accountToNumber;
}
