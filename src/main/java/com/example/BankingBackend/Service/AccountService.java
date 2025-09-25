package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@Service
public interface AccountService {
    Map<String, Long> accountStats();

    List<Account> getAllAccounts();
    Account getAccountById(Long accountId);

    Account createAcc(Account account);
     List<Account> getAccountByCustId(String custId);
}
