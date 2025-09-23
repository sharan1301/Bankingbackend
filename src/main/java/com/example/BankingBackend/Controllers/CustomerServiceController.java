package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.CustomerService;
import com.example.BankingBackend.Repository.CustomerServiceRepo;
import com.example.BankingBackend.Service.EmailService;
//import jakarta.mail.MessagingException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class CustomerServiceController {

    private final CustomerServiceRepo repository;
    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> submitComplaint(@RequestBody CustomerService complaint) {
        repository.save(complaint);
        try {
            emailService.sendComplaintMail(complaint);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Complaint saved but failed to send mail: " + e.getMessage());
        }
        return ResponseEntity.ok("Complaint submitted and emailed successfully");
    }
}
