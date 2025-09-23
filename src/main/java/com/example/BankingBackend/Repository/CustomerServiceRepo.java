package com.example.BankingBackend.Repository;


import com.example.BankingBackend.Model.CustomerService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerServiceRepo extends JpaRepository<CustomerService, Long> {
}
