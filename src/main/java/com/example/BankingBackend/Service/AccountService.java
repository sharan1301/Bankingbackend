package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
@Service
public interface AccountService {
    Map<String, Long> accountStats();
    public List<Account> getAccountsByCustId(String custId);
}
