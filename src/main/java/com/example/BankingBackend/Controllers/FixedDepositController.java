package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.FixedDeposit;
import com.example.BankingBackend.Service.FixedDepositService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/users/FixedDeposit")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowedHeaders = "*", allowCredentials = "true")

public class FixedDepositController {
    private static final Logger logger = LoggerFactory.getLogger(FixedDepositController.class);
    @Autowired
    FixedDepositService fdservice;

    @GetMapping("/home")
    public String homepage(){
        return "homepage";
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<FixedDeposit> addacc(@PathVariable int userId,@RequestBody FixedDeposit request) {
        logger.info("Received request to add FD: {}", request);
        FixedDeposit savedFd = fdservice.AddingAcc(userId,request);
        logger.info("FD saved: {}", savedFd);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFd);
    }

    @GetMapping("/showaccounts")
    public ResponseEntity<Iterable<Account>> showAccounts() {
        return ResponseEntity.ok(fdservice.getAllAccounts());
    }


    @GetMapping("/showall")
    public ResponseEntity<Iterable<FixedDeposit>> getAllFDAccounts(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(fdservice.fetchAllAccounts(date));
    }

    @GetMapping("/calculatematurity")
    public ResponseEntity<Double> calculateMaturity(
            @RequestParam double depositAmount,
            @RequestParam double interestRate,
            @RequestParam int tenureMonths) {

        double maturityAmount = fdservice.calculateMaturityAmount(depositAmount, interestRate, tenureMonths);
        return ResponseEntity.ok(maturityAmount);
    }

    @GetMapping("/check-withdrawal")
    public ResponseEntity<String> checkWithdrawal(@RequestParam Long fdId,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String message = fdservice.previewWithdrawal(fdId,date);
        return ResponseEntity.ok(message);
    }

    // Step 2: Confirm withdrawal (final closure)
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawFD(@RequestParam Long fdId,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        double amount = fdservice.withdrawFD(fdId,date);
        return ResponseEntity.ok("Withdrawal successful! Amount credited: " + amount);
    }
}