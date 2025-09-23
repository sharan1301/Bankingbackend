package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Card;

// DTO to prevent lazy-loading issues
public class CardDTO {
    private Long cardId;
    private String cardNumber;
    private Integer cvv;
    private String expiryDate; // send as string like "MM/yyyy"
    private String cardType;
    private String status;
    private Long accountId;

    public CardDTO(Card card) {
        this.cardId = card.getCardId();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.expiryDate = card.getExpiryDate().getMonthValue() + "/" + card.getExpiryDate().getYear();
        this.cardType = card.getCardType().name();
        this.status = card.getStatus().name();
        this.accountId = card.getAccount() != null ? card.getAccount().getAccountId() : null;
    }

    // getters
    public Long getCardId() { return cardId; }
    public String getCardNumber() { return cardNumber; }
    public Integer getCvv() { return cvv; }
    public String getExpiryDate() { return expiryDate; }
    public String getCardType() { return cardType; }
    public String getStatus() { return status; }
    public Long getAccountId() { return accountId; }
}