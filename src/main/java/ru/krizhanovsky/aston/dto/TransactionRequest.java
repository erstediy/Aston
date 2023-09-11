package ru.krizhanovsky.aston.dto;

import lombok.*;
import ru.krizhanovsky.aston.model.TransactionType;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransactionRequest {
    private BigDecimal amount;
    private TransactionType type;
    private String pin;
    private String accountFromNumber;
    private String accountToNumber;
}
