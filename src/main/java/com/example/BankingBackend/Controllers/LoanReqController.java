package com.example.BankingBackend.Controllers;


import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Service.LoanReqService;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("/users")
public class LoanReqController {

    @Autowired
    LoanReqService loanReqService;

    @PostMapping("/loan-requests")
    public ResponseEntity<?> createLoanRequest(@RequestBody Map<String, Object> requestBody) {
        try {
            loanReqService.createLoanRequest(requestBody);
            return ResponseEntity.ok("Loan request submitted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}