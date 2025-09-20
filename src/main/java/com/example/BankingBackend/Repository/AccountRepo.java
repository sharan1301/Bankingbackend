package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account,Long> {
    @Query("SELECT a FROM Account a WHERE a.user.userId = :userId")
    Optional<Account> findByUserId(@Param("userId") int userId);
}
