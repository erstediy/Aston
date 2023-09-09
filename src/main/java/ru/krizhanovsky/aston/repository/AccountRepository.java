package ru.krizhanovsky.aston.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.krizhanovsky.aston.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
