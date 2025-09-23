package com.example.BankingBackend.Service;

import com.example.BankingBackend.Controllers.CardDTO;
import com.example.BankingBackend.Model.*;
import com.example.BankingBackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditCardApplicationService {

    @Autowired
    private CreditCardApplicationRepo applicationRepository;

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private UsersRepo usersRepository;

    @Autowired
    private AccountRepo accountRepository;

    // Save user application
    public CreditCardApplication apply(CreditCardApplication application) {
        application.setStatus(CreditCardApplication.ApplicationStatus.UNDER_REVIEW);
        application.setSubmittedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    // Get all applications
    public List<CreditCardApplication> getAllApplications() {
        return applicationRepository.findAll();
    }

    // Approve application and create card
//    public Card approveApplication(Long applicationId, Long userId, Long accountNo) {
//        CreditCardApplication application = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        if (application.getStatus() == CreditCardApplication.ApplicationStatus.APPROVED) {
//            throw new RuntimeException("Application already approved");
//        }
//
//        application.setStatus(CreditCardApplication.ApplicationStatus.APPROVED);
//        application.setApprovedAt(LocalDateTime.now());
//        applicationRepository.save(application);
//
//        // Create Card for user
//        Users user = usersRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Account account = accountRepository.findById(accountNo)
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//
//        Card card = Card.builder()
//                .user(user)
//                .account(account)
//                .cardNumber(generateCardNumber())
//                .cvv(generateCVV())
//                .expiryDate(generateExpiryDate())
//                .status(Card.CardStatus.ACTIVE)
//                .cardType(mapCardType(application.getCardType()))
//                .build();
//
//        return cardRepository.save(card);
//    }


    public Card handleApplication(Long applicationId, boolean approve, String remark) {

        CreditCardApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Users user = usersRepository.findByAadhaarNumber(application.getAadhaarNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (approve) {
            if (application.getStatus() == CreditCardApplication.ApplicationStatus.APPROVED) {
                throw new RuntimeException("Application already approved");
            }

            application.setStatus(CreditCardApplication.ApplicationStatus.APPROVED);
            application.setApprovedAt(LocalDateTime.now());
            applicationRepository.save(application);

            Card card = Card.builder()
                    .user(user)
                    .account(account)
                    .cardNumber(generateCardNumber())
                    .cvv(generateCVV())
                    .expiryDate(generateExpiryDate())
                    .status(Card.CardStatus.ACTIVE)
                    .cardType(mapCardType(application.getCardType()))
                    .build();

            return cardRepo.save(card);

        } else {
            application.setStatus(CreditCardApplication.ApplicationStatus.REJECTED);
            application.setRemarks(remark != null ? remark : "Application rejected");
            applicationRepository.save(application);

            return null;
        }
    }



    // --- Helper Methods ---
    private String generateCardNumber() {
        long number = (long)(Math.random() * 1_0000_0000_0000_0000L);
        return String.format("%016d", number);
    }

    private int generateCVV() {
        return (int)(Math.random() * 900) + 100;
    }

    private java.time.LocalDate generateExpiryDate() {
        return java.time.LocalDate.now().plusYears(5);
    }

    private Card.CardType mapCardType(CreditCardApplication.CardType type) {
        switch(type) {
            case GOLD: return Card.CardType.DEBIT; // Example mapping
            case PLATINUM: return Card.CardType.CREDIT;
            default: return Card.CardType.PREPAID;
        }
    }
}
