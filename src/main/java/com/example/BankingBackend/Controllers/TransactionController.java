<<<<<<< HEAD
package org.example.Controllers;
import org.example.Model.Account;
import org.example.Model.Transaction;
import org.example.Request.TransactionRequest;
import org.example.Services.TransactionService;
=======
package com.example.BankingBackend.Controllers;


import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Transaction;
import com.example.BankingBackend.Service.TransactionService;
>>>>>>> sharan
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/transfers")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    // 1. Bank Transfer
<<<<<<< HEAD
    @PostMapping("/bank")
=======
    @PostMapping("/{accountId}/bank")
>>>>>>> sharan
    public ResponseEntity<Transaction> createBankTransfer(@RequestBody Transaction transfer) {

        Transaction saved = transactionService.saveBankTransfer(transfer);
        return ResponseEntity.ok(saved);
    }

    // 2. Self Transfer
<<<<<<< HEAD
    @PostMapping("/self")
=======
    @PostMapping("/{accountId}/self")
>>>>>>> sharan
    public ResponseEntity<Transaction> createSelfTransfer(@RequestBody Transaction transfer) {
        Transaction saved = transactionService.saveSelfTransfer(transfer);
        return ResponseEntity.ok(saved);
    }

    // 3. Payee Transfer
<<<<<<< HEAD
    @PostMapping("/payee")
=======
    @PostMapping("/{accountId}/payee")
>>>>>>> sharan
    public ResponseEntity<Transaction> createPayeeTransfer(@RequestBody Transaction transfer) {
        Transaction saved = transactionService.savePayeeTransfer(transfer);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/from/{fromAccNo}")
    public List<Transaction> getTransfersByFromAccNo(@PathVariable int fromAccNo) {
        return transactionService.getTransfersByFromAccNo(fromAccNo);
    }
<<<<<<< HEAD
=======

>>>>>>> sharan
    @GetMapping("/for/{fromAccNo}")
    public Account getFromAccNo(@PathVariable Long fromAccNo) {
        return transactionService.getAccountByAccNo(fromAccNo);
    }
<<<<<<< HEAD
=======

>>>>>>> sharan
    @GetMapping("/to/{toAccNo}")
    public List<Transaction> getTransfersByToAccNo(@PathVariable int toAccNo) {
        return transactionService.getTransfersByToAccNo(toAccNo);
    }
<<<<<<< HEAD
    @GetMapping("/all")
    public List<Transaction> getTransfers(){
=======

    @GetMapping("/all")
    public List<Transaction> getTransfers() {
>>>>>>> sharan
        return transactionService.getTransfers();
    }

    @PutMapping("/add")
    public Transaction saveTransaction(@RequestBody Transaction fundTransfer) {
        return transactionService.saveTransaction(fundTransfer);
    }
<<<<<<< HEAD

}
=======
}

>>>>>>> sharan
