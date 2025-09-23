package com.example.BankingBackend.Controllers;


import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Service.LoanReqService;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("/users")
public class LoanReqController {

    @Autowired
    LoanReqService loanReqService;

    @PostMapping("/{userId}/loan-requests")
    public ResponseEntity<LoanRequests> createLoanRequest(
            @PathVariable Integer userId,
            @RequestBody LoanRequests request) {

        LoanRequests savedRequest = loanReqService.createLoanRequest(userId, request);

        return ResponseEntity.ok(savedRequest);
    }

}