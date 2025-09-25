package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Card;
import com.example.BankingBackend.Model.CreditCardApplication;
import com.example.BankingBackend.Repository.CreditCardApplicationRepo;
import com.example.BankingBackend.Service.CreditCardApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import java.util.List;

@RestController
@RequestMapping("users/credit-card")
public class CreditCardApplicationController {

    @Autowired
    private CreditCardApplicationService service;
    @Autowired
    private CreditCardApplicationRepo repository;
    // User applies for credit card
    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody CreditCardApplication application) {
        return service.apply(application);
    }

    // Get all applications (admin)
//    @GetMapping("/all")
//    public List<CreditCardApplication> getAllApplications() {
//        return service.getAllApplications();
//    }

    // Admin approves application and creates card
//    @PostMapping("/approve/{applicationId}")
//    public Card approveApplication(@PathVariable Long applicationId,
//                                   @RequestParam Long userId,
//                                   @RequestParam Long accountNo) {
//        return service.approveApplication(applicationId, userId, accountNo);
//    }









    @GetMapping("/status/{aadhaar}")
    public ResponseEntity<?> getApplicationStatus(@PathVariable String aadhaar) {
        List<CreditCardApplication> applications = repository.findByAadhaarNumber(aadhaar);

        if (applications.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "No applications found"));
        }

        List<Map<String, Object>> result = applications.stream().map(app -> {
            Map<String, Object> map = new HashMap<>();
            map.put("fullName", app.getFullName());
            map.put("aadhaarNumber", app.getAadhaarNumber());
            map.put("status", app.getStatus());
            map.put("cardType", app.getCardType());
            map.put("remark", app.getRemarks());
            return map;
        }).toList();

        return ResponseEntity.ok(result);
    }


}
