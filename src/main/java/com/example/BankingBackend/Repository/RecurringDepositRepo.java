package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.RecurringDeposit;
import org.springframework.data.repository.CrudRepository;

public interface RecurringDepositRepo extends CrudRepository<RecurringDeposit,Long> {

}