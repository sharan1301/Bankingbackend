package com.example.BankingBackend.Repository;

import com.example.BankingBackend.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {

    // find card by card number
    Optional<Card> findByCardNumber(String cardNumber);

    // check if card exists by card number
    boolean existsByCardNumber(String cardNumber);
}