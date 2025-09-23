package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    @Query("SELECT a FROM Account a WHERE a.user.userId = :userId")
    Optional<Account> findByUserId(@Param("userId") int userId);
    Account findByUser(Users user);
    long countByStatusAndUserIsNotNull(Account.AccountStatus status);
    long countByStatus(Account.AccountStatus status);
    long countByAccountType(Account.AccountType type);
    long countByAccountTypeAndStatus(Account.AccountType type, Account.AccountStatus status);
    Optional<Account> findByAccountNumber(Long accountNumber);

}
