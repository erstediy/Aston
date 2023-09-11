package ru.krizhanovsky.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.krizhanovsky.aston.model.Account;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Account saveAndFlush(Account entity);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query("select a from Account a where a.balance = (select max(b.balance) from Account b)")
    List<Account> findWithMaxBalance();
}
