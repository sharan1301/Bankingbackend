package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountActionService {
    @Autowired
    AccountRepo accountRepo;

    public ResponseEntity<?> freezeAccount(Long id) {
        Optional<Account> accountOpt=accountRepo.findById(id);

        if (!accountOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account with ID " + id + " not found");
        }
        Account account=accountOpt.get();
        if (account.getStatus() == Account.AccountStatus.ACTIVE) {
            account.setStatus(Account.AccountStatus.FROZEN);
            accountRepo.save(account);
            return ResponseEntity.ok(Map.of(
                    "account", account
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account cannot be frozen because it is " + account.getStatus());
        }
    }

    public List<Account> getAllAccount() {
        return accountRepo.findAll();
    }

    public ResponseEntity<?> unfreezeAccount(Long id) {
        Optional<Account> accountOpt=accountRepo.findById(id);

        if (!accountOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Account with ID " + id + " not found");
        }
        Account account=accountOpt.get();
        if (account.getStatus() == Account.AccountStatus.FROZEN) {
            account.setStatus(Account.AccountStatus.ACTIVE);
            accountRepo.save(account);
            return ResponseEntity.ok(Map.of(
                    "account", account
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account is already active");
        }
    }
}
