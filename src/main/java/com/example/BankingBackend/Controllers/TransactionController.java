package com.example.BankingBackend.Controllers;
import com.example.BankingBackend.Model.Transaction;
import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Service.TransactionService;
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
    @PostMapping("/bank")
    public ResponseEntity<Transaction> createBankTransfer(@RequestBody Transaction transfer) {

        Transaction saved = transactionService.saveBankTransfer(transfer);
        return ResponseEntity.ok(saved);
    }

    // 2. Self Transfer
    @PostMapping("/self")
    public ResponseEntity<Transaction> createSelfTransfer(@RequestBody Transaction transfer) {
        Transaction saved = transactionService.saveSelfTransfer(transfer);
        return ResponseEntity.ok(saved);
    }

    // 3. Payee Transfer
    @PostMapping("/payee")
    public ResponseEntity<Transaction> createPayeeTransfer(@RequestBody Transaction transfer) {
        Transaction saved = transactionService.savePayeeTransfer(transfer);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/from/{fromAccNo}")
    public List<Transaction> getTransfersByFromAccNo(@PathVariable int fromAccNo) {
        return transactionService.getTransfersByFromAccNo(fromAccNo);
    }
    @GetMapping("/for/{fromAccNo}")
    public Account getFromAccNo(@PathVariable Long fromAccNo) {
        return transactionService.getAccountByAccNo(fromAccNo);
    }
    @GetMapping("/to/{toAccNo}")
    public List<Transaction> getTransfersByToAccNo(@PathVariable int toAccNo) {
        return transactionService.getTransfersByToAccNo(toAccNo);
    }
    @GetMapping("/all")
    public List<Transaction> getTransfers(){
        return transactionService.getTransfers();
    }

    @PutMapping("/add")
    public Transaction saveTransaction(@RequestBody Transaction fundTransfer) {
        return transactionService.saveTransaction(fundTransfer);
    }

}