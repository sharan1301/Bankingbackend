package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("users/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/all")
    public List<Account> getAllUsers() {
        return accountService.getAllAccounts();
    }

    @PostMapping("/add")
    public ResponseEntity<Account> addAccount(@RequestBody Account account) {
        Account savedAccount = accountService.createAcc(account);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
//    @GetMapping("/by-custid/{custId}")
//    public ResponseEntity<Account> getAccountByCustId(@PathVariable String custId) {
//        Account account = accountService.getAccountByCustId(custId);
//        if (account != null) {
//            return ResponseEntity.ok(account);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//    }
    @GetMapping("/by-custid/{custId}")
    public ResponseEntity<List<Account>> getAccountByCustId(@PathVariable String custId) {
        List<Account> accounts = accountService.getAccountByCustId(custId);
        if (!accounts.isEmpty()) {
            return ResponseEntity.ok(accounts);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
