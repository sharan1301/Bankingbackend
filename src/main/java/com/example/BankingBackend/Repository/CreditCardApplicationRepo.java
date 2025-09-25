package com.example.BankingBackend.Repository;


import com.example.BankingBackend.Model.CreditCardApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreditCardApplicationRepo extends JpaRepository<CreditCardApplication, Long> {
    Optional<CreditCardApplication> findByApplicationId(String applicationId);
    List<CreditCardApplication> findByAadhaarNumber(String aadhaarNumber);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM credit_card_application c " +
            "WHERE c.account_id = :accountId AND c.status = 'UNDER_REVIEW'",
            nativeQuery = true)
    boolean existsPendingApplication(@Param("accountId") Long accountId);
    List<CreditCardApplication> findByStatus(CreditCardApplication.ApplicationStatus status);
}