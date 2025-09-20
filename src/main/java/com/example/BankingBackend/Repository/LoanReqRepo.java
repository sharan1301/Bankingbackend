package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.LoanRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanReqRepo extends JpaRepository<LoanRequests,Long> {
    List<LoanRequests> findByStatus(LoanRequests.RequestStatus status);
}
