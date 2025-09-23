package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Payee;
import com.example.BankingBackend.Repository.PayeeRepo;
import com.example.BankingBackend.Service.PayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payees")
public class PayeeController {

    @Autowired
    private PayeeService payeeService;

    @Autowired
    private PayeeRepo payeeRepository;

    @PostMapping("/{accountId}/add")
    public ResponseEntity<Payee> addPayee(@PathVariable Long accountId, @RequestBody Payee payee) {
        // Pass the accountId to the service and add the Payee
        Payee savedPayee = payeeService.addPayee(payee, accountId);
        return new ResponseEntity<>(savedPayee, HttpStatus.CREATED);
    }

//    @PostMapping("/delete")
@PostMapping("/update/{payeeId}")
public ResponseEntity<Payee> updatePayee(@PathVariable int payeeId, @RequestBody Payee payee) {
    Optional<Payee> existingPayeeOpt = payeeService.getPayeeById(payeeId);
    if (existingPayeeOpt.isPresent()) {
        Payee existingPayee = existingPayeeOpt.get();
        payee.setPayeeId(payeeId);
        // Preserve the existing associated Account to avoid null reference
        payee.setAccount(existingPayee.getAccount());
        Payee updatedPayee = payeeService.updatePayee(payee);
        return new ResponseEntity<>(updatedPayee, HttpStatus.OK);
    } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}



    @GetMapping("/all")
    public ResponseEntity<List<Payee>> getAllPayees() {
        List<Payee> payees = payeeService.getAllPayees();
        if (!payees.isEmpty()) {
            return new ResponseEntity<>(payees, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Payee>> getPayeesByAccountId(@PathVariable Long accountId) {
        List<Payee> payees = payeeService.getPayeesByAccountId(accountId);
        if (!payees.isEmpty()) {
            return new ResponseEntity<>(payees, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/account/{payeeId}")
    public ResponseEntity<Void> deletePayee(@PathVariable int payeeId) {
        Optional<Payee> existingPayee = payeeService.getPayeeById(payeeId);
        if (existingPayee.isPresent()) {
            payeeService.deletePayee(payeeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
