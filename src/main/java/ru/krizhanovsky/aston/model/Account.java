package ru.krizhanovsky.aston.model;

import javax.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;

@Entity
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountNumber")
    private final Long accountNumber;

    @Column(name = "Name")
    private String name;

    @Column(name = "PIN", nullable = false)
    private String pin;

    @Column(name = "Balance", columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private BigDecimal balance;

    /**
     * Изменить состояние баланса
     * @param amount сумма изменения
     */
    public void editBalance(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Отрицательное значение недопустимо");
        }

        balance = balance.add(amount);
    }

    /**
     * Установить ПИН-код
     * @param pin ПИН-код
     */
    public void setPin(String pin) {
        this.pin = BCrypt.hashpw(pin, BCrypt.gensalt());
    }

    /**
     * Проверить ПИН-код
     * @param inputPin ПИН-код
     * @return true - соответствует, false - несоответствует
     */
    public boolean checkPin(String inputPin) {
        return BCrypt.checkpw(inputPin, pin);
    }

    /**
     * Установить имя
     * @param name Имя
     */
    public void setName(String name) {
        this.name = name;
    }

}
