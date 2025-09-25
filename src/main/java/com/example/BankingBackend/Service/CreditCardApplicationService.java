package com.example.BankingBackend.Service;

import com.example.BankingBackend.Controllers.CardDTO;
import com.example.BankingBackend.Model.*;
import com.example.BankingBackend.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public ResponseEntity<?> apply(CreditCardApplication application) {
        Account account = accountRepository.findAccountByAccountNumber(application.getAccountNumber());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Account not found"));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println(email);
        System.out.println(application.getEmailAddress());
        List<Users> User = usersRepository.findByEmailIgnoreCase(email);
             if(User.isEmpty())
              throw new RuntimeException("Logged-in user not found");
         Users loggedInUser=User.get(0);

        if (application.getEmailAddress()==(account.getUser().getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "The user has no link to this account."));
        }

        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Card request cannot be created. Account status is " + account.getStatus());
        }

        application.setAccount(account);
        application.setStatus(CreditCardApplication.ApplicationStatus.UNDER_REVIEW);
        application.setSubmittedAt(LocalDateTime.now());

        CreditCardApplication savedApp = applicationRepository.save(application);

        return ResponseEntity.ok(savedApp);
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

    public Card handleApplication(Long applicationId) {
        CreditCardApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        Account account = application.getAccount();
        if (account == null) {
            throw new RuntimeException("Account not linked to application");
        }

        Users user = account.getUser();
        if (user == null) {
            throw new RuntimeException("User not linked to account");
        }

        // Default action: Approve
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
    }


    public ResponseEntity<?> rejectApplication(Long applicationId) {
        CreditCardApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (application.getStatus() == CreditCardApplication.ApplicationStatus.REJECTED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Application already rejected"));
        }

        if (application.getStatus() == CreditCardApplication.ApplicationStatus.APPROVED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Cannot reject an already approved application"));
        }

        application.setStatus(CreditCardApplication.ApplicationStatus.REJECTED);
       // application.setRemarks(remarks != null ? remarks : "Application rejected by admin");
        application.setApprovedAt(LocalDateTime.now());

        CreditCardApplication updatedApp = applicationRepository.save(application);

        return ResponseEntity.ok(
                Map.of(
                        "applicationId", updatedApp.getApplicationId(),
                        "status", updatedApp.getStatus().name(),
                        "remarks", updatedApp.getRemarks()
                )
        );
    }





    // --- Helper Methods ---
    private String generateCardNumber() {
        long number = (long)(Math.random() * 1_0000_0000_0000_0000L);
        return String.format("%016d", number);
    }

    private int generateCVV() {
        return (int)(Math.random() * 900) + 100;
    }

    private LocalDate generateExpiryDate() {
        return LocalDate.now().plusYears(5);
    }

    private Card.CardType mapCardType(CreditCardApplication.CardType type) {
        switch(type) {
            case GOLD: return Card.CardType.DEBIT; // Example mapping
            case PLATINUM: return Card.CardType.CREDIT;
            default: return Card.CardType.PREPAID;
        }
    }
}