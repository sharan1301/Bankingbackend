package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Autopay;
import com.example.BankingBackend.Model.Payee;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.PayeeRepo;
import com.example.BankingBackend.Service.AutopayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/autopay")
public class AutopayController {

    @Autowired
    private AutopayService autopayService;

    @Autowired
    private AccountRepo accountRepository;

    @Autowired
    private PayeeRepo payeeRepository;

    @PostMapping("/create")
    public Autopay createAutopay(@RequestBody Map<String, Object> request) {
        Long senderAccNo = Long.valueOf(request.get("senderAccount").toString());
        Long receiverPayeeAccNo = Long.valueOf(request.get("receiverPayeeAccNo").toString());
        Double amount = Double.valueOf(request.get("amount").toString());
        Long frequencySeconds = Long.valueOf(request.get("frequencySeconds").toString());
        LocalDateTime startDate = LocalDateTime.parse(request.get("startDate").toString());
        LocalDateTime endDate = request.containsKey("endDate") ?
                LocalDateTime.parse(request.get("endDate").toString()) : null;

        Account sender = accountRepository.findAccountByAccountNumber(senderAccNo);
        if (sender == null) throw new RuntimeException("Sender account not found");

        Payee receiver = payeeRepository.findByPayeeAccNo(receiverPayeeAccNo);
        if (receiver == null || receiver.getAccount() == null)
            throw new RuntimeException("Receiver account not found");

        return autopayService.createAutopay(sender, receiver, amount, frequencySeconds, startDate, endDate);
    }

    @DeleteMapping("/cancel/{id}")
    public String cancelAutopay(@PathVariable Long id) {
        autopayService.cancelAutopay(id);
        return "Autopay cancelled successfully";
    }
}
