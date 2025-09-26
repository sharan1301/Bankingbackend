package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.RecurringDeposit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecurringDepositRepo extends CrudRepository<RecurringDeposit,Long> {
    List<RecurringDeposit> findByUser_UserId(Long userId);
}