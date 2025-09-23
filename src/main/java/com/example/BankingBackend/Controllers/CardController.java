package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Card;
import com.example.BankingBackend.Repository.CardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardRepo cardRepo;

    // Get all cards safely
    @GetMapping("/all")
    public ResponseEntity<List<CardDTO>> getAllCards() {
        List<Card> cards = cardRepo.findAll();
        List<CardDTO> cardDTOs = cards.stream()
                .map(CardDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cardDTOs);
    }

    // Get card by ID
    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id) {
        return cardRepo.findById(id)
                .map(CardDTO::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update card status
    @PutMapping("/{id}/status")
    public ResponseEntity<CardDTO> updateCardStatus(@PathVariable Long id, @RequestParam String status) {
        return cardRepo.findById(id)
                .map(card -> {
                    card.setStatus(Card.CardStatus.valueOf(status));
                    cardRepo.save(card);
                    return new CardDTO(card);
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a card
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        if (cardRepo.existsById(id)) {
            cardRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}