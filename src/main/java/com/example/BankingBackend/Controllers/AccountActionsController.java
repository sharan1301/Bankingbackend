package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Service.AccountActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("/admin/accounts")
public class AccountActionsController {
    @Autowired
    AccountActionService accountActionService;
    @PutMapping("/{id}/freeze")
   public ResponseEntity<?> freezeAccount(@PathVariable Long id){
        return accountActionService.freezeAccount(id);
    }
    @PutMapping("{id}/unfreeze")
    public ResponseEntity<?> unfreezeAccount(@PathVariable Long id){
        return accountActionService.unfreezeAccount(id);
    }
    @GetMapping("/all")
    public List<Account> getAllAccounts(){
        return accountActionService.getAllAccount();
    }



}
