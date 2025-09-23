package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    // Custom query to find transactions where the sender account number matches
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber")
    List<Transaction> findByFromAccNo(int accountNumber);

    // Custom query to find transactions where the receiver account number matches
    @Query("SELECT t FROM Transaction t WHERE t.recvAcc.accountNumber = :accountNumber")
    List<Transaction> findByToAccNo(int accountNumber);

    // Find all transactions (standard JPA repository method)
    List<Transaction> findAll();
}

