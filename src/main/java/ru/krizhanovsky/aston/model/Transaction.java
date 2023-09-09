package ru.krizhanovsky.aston.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@Table(name = "Transactions")
public class Transaction {
    @Id
    @Column(name = "TransactionID")
    private UUID id;

    @Column(name = "AccountNumber")
    private Integer accountNumber;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "TransactionType")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "TransactionDate", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
}
