package com.example.BankingBackend.Controllers;

class ApplicationResponseDTO {
    private String applicationId;
    private String status;
    private String remarks;
    private CardDTO card; // null if rejected

    public ApplicationResponseDTO(String applicationId, String status, String remarks, CardDTO card) {
        this.applicationId = applicationId;
        this.status = status;
        this.remarks = remarks;
        this.card = card;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getStatus() {
        return status;
    }

    public String getRemarks() {
        return remarks;
    }

    public CardDTO getCard() {
        return card;
    }
}