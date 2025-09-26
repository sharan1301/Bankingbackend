package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Loan;
import com.example.BankingBackend.Model.Transaction;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.LoanRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserDashboardController {

    @Autowired
    AccountService accountService;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    LoanRepo loanRepo;

    @Autowired
    TransactionService transactionService;

    @GetMapping("/accounts")
    public ResponseEntity<?> getAccountsForCust(@RequestParam String custId) {
        List<Account> accounts = accountService.getAccountsByCustId(custId);

        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No accounts found for this custId");
        }

        List<Map<String, Object>> response = new ArrayList<>();
        for (Account acc : accounts) {
            Map<String, Object> accData = new HashMap<>();
            accData.put("userId", acc.getUser().getUserId());
            accData.put("accountNumber", acc.getAccountNumber());
            accData.put("accountType", acc.getAccountType());
            accData.put("balance", acc.getBalance());
            response.add(accData);
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/status")
    public ResponseEntity<List<Loan>> getLoanStatus(@RequestParam String custId) {
        List<Users> usersList = usersRepo.findAllByCustId(custId);
        if (usersList.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<Integer> userIds = usersList.stream()
                .map(Users::getUserId)
                .collect(Collectors.toList());
        List<Loan> loans = loanRepo.findLoansByUserIds(userIds);
        return ResponseEntity.ok(loans);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam Long custId) {
        List<Transaction> transactions = transactionService.getTransactionsByCustId(custId);
        return ResponseEntity.ok(transactions);
    }




}
