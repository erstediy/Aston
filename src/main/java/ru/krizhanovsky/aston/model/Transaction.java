package ru.krizhanovsky.aston.model;

import javax.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "Transactions")
public class Transaction {
    @Id
    @Column(name = "TransactionID")
    private UUID id;

    @Column(name = "Account_From_Number")
    private String accountFromNumber;

    @Column(name = "Account_To_Number")
    private String accountToNumber;

    @Column(name = "Amount")
    private BigDecimal amount;

    @Column(name = "Transaction_Type")
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "Transaction_Date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
}
