package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.UserRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserReqRepo extends JpaRepository<UserRequests,Integer> {
    List<UserRequests> findByStatus(String status);
    Long countByStatus(String status);
    Optional<UserRequests> findByAadhaarNumberAndStatus(String aadhaarNumber, String status);
}
