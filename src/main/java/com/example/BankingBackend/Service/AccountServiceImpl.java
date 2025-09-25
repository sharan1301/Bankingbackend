package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
  @Autowired
    AccountRepo accountRepo;
    public Map<String, Long> accountStats() {
        Map<String, Long> map = new HashMap<>();
        map.put("activeAccounts", Long.valueOf(accountRepo.countByStatusAndUserIsNotNull(Account.AccountStatus.ACTIVE)));
        map.put("activeCount", Long.valueOf(accountRepo.countByStatus(Account.AccountStatus.ACTIVE)));
        map.put("inactiveCount", Long.valueOf(accountRepo.countByStatus(Account.AccountStatus.INACTIVE)));
        map.put("frozenCount", Long.valueOf(accountRepo.countByStatus(Account.AccountStatus.FROZEN)));
        map.put("savingsCount", Long.valueOf(accountRepo.countByAccountType(Account.AccountType.SAVINGS)));
        map.put("currentCount", Long.valueOf(accountRepo.countByAccountType(Account.AccountType.CURRENT)));
        map.put("salaryCount", Long.valueOf(accountRepo.countByAccountType(Account.AccountType.SALARY)));
        map.put("businessCount", Long.valueOf(accountRepo.countByAccountType(Account.AccountType.BUSINESS)));
        return map;
    }

    @Override
    public List<Account> getAccountsByCustId(String custId) {
        return accountRepo.findAllByCustId(custId);
    }
}
