package ru.krizhanovsky.aston.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class AccountInfo {
    private String name;
    private BigDecimal balance;
}
