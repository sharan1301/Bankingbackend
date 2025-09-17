package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.UserRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestsRepo extends JpaRepository<UserRequests,Integer> {
    List<UserRequests> findByStatus(String status);
}
