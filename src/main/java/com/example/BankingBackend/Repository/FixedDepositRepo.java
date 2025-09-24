package com.example.BankingBackend.Repository;


import com.example.BankingBackend.Model.FixedDeposit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixedDepositRepo extends CrudRepository<FixedDeposit,Long> {

}
