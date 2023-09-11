package ru.krizhanovsky.aston.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfo {
    private String name;
    private BigDecimal balance;
    private String pin;
}
