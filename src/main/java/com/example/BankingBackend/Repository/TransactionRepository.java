////package org.example.Repository;
////
////import org.springframework.data.jpa.repository.JpaRepository;
////
////public interface TransactionRepository extends JpaRepository {
////}
////import org.example.Model.FundTransfer
////import org.springframework.data.jpa.repository.JpaRepository;
////
////import java.util.List;
////
////
////
////
////
////
//
//package org.example.Repository;
//
//import org.example.Model.Account;
//import org.example.Model.Transaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.List;
//
//public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
//    // You can add custom queries later, e.g. findByFromAccNo or findByToAccNo
//
//    List<Transaction> findByFromAccNo(int account);
//    List<Transaction> findByToAccNo(int revAcc);
//    List<Transaction> findAll();
//}


package org.example.Repository;

import org.example.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Custom query to find transactions where the sender account number matches
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNumber = :accountNumber")
    List<Transaction> findByFromAccNo(int accountNumber);

    // Custom query to find transactions where the receiver account number matches
    @Query("SELECT t FROM Transaction t WHERE t.recvAcc.accountNumber = :accountNumber")
    List<Transaction> findByToAccNo(int accountNumber);

    // Find all transactions (standard JPA repository method)
    List<Transaction> findAll();
}
