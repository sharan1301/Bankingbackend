package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.RecurringDeposit;
import com.example.BankingBackend.Service.RecurringDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/users/RecurringDeposit")
@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
public class RecurringDepositController {

    @Autowired
    private RecurringDepositService rdService;

    @GetMapping("/home")
    public String homepage(){
        return "homepage";
    }

    @PostMapping("/{userId}/add")
    public ResponseEntity<RecurringDeposit> addacc(@PathVariable int userId,@RequestBody RecurringDeposit request) {
        RecurringDeposit savedFd = rdService.AddingAcc(userId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFd);
    }

    @GetMapping("/showaccounts")
    public ResponseEntity<Iterable<Account>> showAccounts() {
        return ResponseEntity.ok(rdService.getAllAccounts());
    }


    @GetMapping("/showall")
    public ResponseEntity<Iterable<RecurringDeposit>> getAllFDAccounts(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(rdService.fetchAllAccountswithfine(date));
    }

    @GetMapping("/rdshowall/{userId}")
    public ResponseEntity<?> getAllRDAccountsByUser(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(rdService.fetchAllAccountsWithFine(userId, date));
    }


    @GetMapping("/calculatematurity")
    public ResponseEntity<Double> calculateMaturity(
            @RequestParam double depositAmount,
            @RequestParam double interestRate,
            @RequestParam int tenureMonths) {

        double maturityAmount = rdService.calculateMaturityAmount(depositAmount, interestRate, tenureMonths);
        return ResponseEntity.ok(maturityAmount);
    }

    @PostMapping("/pay")
    public ResponseEntity<String> payInstallment(
            @RequestParam Long rdId,
            @RequestParam Double amount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate) {

        String result = rdService.payInstallment(rdId, amount,paymentDate);
        return ResponseEntity.ok(result);
    }

    // Step 1: Preview withdrawal (show user message before finalizing)
    @GetMapping("/check-withdrawal/{rdId}/{date}")
    public ResponseEntity<String> checkWithdrawal(@PathVariable Long rdId,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String message = rdService.previewWithdrawal(rdId,date);
        return ResponseEntity.ok(message);
    }

    // Step 2: Confirm withdrawal (final closure)
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawRD(@RequestParam Long rdId,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        double amount = rdService.withdrawRD(rdId,date);
        return ResponseEntity.ok("Withdrawal successful! Amount credited: " + amount);
    }
}

