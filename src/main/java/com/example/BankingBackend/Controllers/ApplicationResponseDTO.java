package com.example.BankingBackend.Controllers;

class ApplicationResponseDTO {
    private String applicationId;
    private String status;
    private CardDTO card; // null if rejected

    public ApplicationResponseDTO(String applicationId, String status,  CardDTO card) {
        this.applicationId = applicationId;
        this.status = status;
        this.card = card;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getStatus() {
        return status;
    }



    public CardDTO getCard() {
        return card;
    }
}