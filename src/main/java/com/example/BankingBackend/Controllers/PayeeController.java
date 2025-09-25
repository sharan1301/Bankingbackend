
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

@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("users/payees")
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
    @DeleteMapping("/delete/{payeeId}")
    public ResponseEntity<?> deletePayee(@PathVariable int payeeId) {
        boolean deleted = payeeService.deletePayee(payeeId);
        if (deleted) {
            return ResponseEntity.ok(" Payee Deleted"); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
