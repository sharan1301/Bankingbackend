package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Autopay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutopayRepo extends JpaRepository<Autopay, Long> {
    List<Autopay> findByActiveTrue();
}
