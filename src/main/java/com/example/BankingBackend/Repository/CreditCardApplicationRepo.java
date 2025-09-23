package com.example.BankingBackend.Repository;


import com.example.BankingBackend.Model.CreditCardApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditCardApplicationRepo extends JpaRepository<CreditCardApplication, Long> {
    Optional<CreditCardApplication> findByApplicationId(String applicationId);
    List<CreditCardApplication> findByAadhaarNumber(String aadhaarNumber);
}