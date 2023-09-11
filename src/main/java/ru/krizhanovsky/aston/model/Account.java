package ru.krizhanovsky.aston.model;

import javax.persistence.*;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.mindrot.jbcrypt.BCrypt;
import ru.krizhanovsky.aston.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;

    @Column(name = "AccountNumber")
    private final String accountNumber;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "PIN_HASH", nullable = false)
    @JsonIgnore
    private String pin;

    @Column(name = "Balance", columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private BigDecimal balance;

    /**
     * Изменить состояние баланса
     *
     * @param amount сумма изменения
     */
    public void editBalance(BigDecimal amount) throws InsufficientFundsException {
        if (balance.add(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Отрицательный баланс недопустим");
        }
        balance = balance.add(amount);
    }

    /**
     * Проверить ПИН-код
     *
     * @return true - соответствует, false - несоответствует
     */
    public String getPin() {
        return pin;
    }

    /**
     * Установить имя
     *
     * @param name Имя
     */
    public void setName(String name) {
        this.name = name;
    }
}
