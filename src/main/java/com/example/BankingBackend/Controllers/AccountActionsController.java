package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Service.AccountActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/accounts")
public class AccountActionsController {
    @Autowired
    AccountActionService accountActionService;
    @PutMapping("/freezeaccount/{id}")
   public ResponseEntity<?> freezeAccount(@PathVariable Long id){
        return accountActionService.freezeAccount(id);
    }
    @PutMapping("/unfreezeaccount/{id}")
    public ResponseEntity<?> unfreezeAccount(@PathVariable Long id){
        return accountActionService.unfreezeAccount(id);
    }
    @GetMapping("/all")
    public List<Account> getAllAccounts(){
        return accountActionService.getAllAccount();
    }



}
