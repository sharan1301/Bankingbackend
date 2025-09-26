package com.example.BankingBackend.Repository;


import com.example.BankingBackend.Model.FixedDeposit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedDepositRepo extends CrudRepository<FixedDeposit,Long> {
    List<FixedDeposit> findByUser_UserId(Long userId);
}
