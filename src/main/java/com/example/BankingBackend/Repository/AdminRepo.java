package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Admin;
import com.example.BankingBackend.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<Admin,Integer> {
    Optional<Admin> findByEmailIgnoreCase(String email);
}
