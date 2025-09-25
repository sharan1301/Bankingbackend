package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserDashboardController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/{custId}/accounts")
    public ResponseEntity<?> getAccountsForCust(@PathVariable String custId) {
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
}
