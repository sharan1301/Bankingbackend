package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.UserRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReqRepo extends JpaRepository<UserRequests,Integer> {
}
