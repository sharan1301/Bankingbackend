<<<<<<< HEAD
package org.example.Services;

import org.example.Model.Account;
import org.example.Model.Payee;
import org.example.Repository.AccountRepository;
import org.example.Repository.PayeeRepository;
=======
package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Payee;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.PayeeRepo;
>>>>>>> sharan
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayeeServiceImpl implements PayeeService {

<<<<<<< HEAD
//    private final PayeeRepository payeeRepository;

    @Autowired
//    public PayeeServiceImpl(PayeeRepository payeeRepository) {
//        this.payeeRepository = payeeRepository;
//    }

    PayeeRepository payeeRepository;

    @Autowired
    AccountRepository accountRepository;
=======
    @Autowired
    PayeeRepo payeeRepository;

    @Autowired
    AccountRepo accountRepository;
>>>>>>> sharan

    @Override
    public Payee addPayee(Payee payee, Long accountId) {
        // Fetch the Account by the accountId from the URL
        Optional<Account> account = accountRepository.findById(accountId);

        if (!account.isPresent()) {
            throw new IllegalArgumentException("Account with ID " + accountId + " does not exist.");
        }

<<<<<<< HEAD
        // Set the Account to the Payee object
        payee.setAccount(account.get());

        // Save and return the Payee
=======
        payee.setAccount(account.get());

>>>>>>> sharan
        return payeeRepository.save(payee);
    }

    @Override
    public List<Payee> findPayeesByAccountId(Long accountId) {
        return payeeRepository.findByAccount_AccountId(accountId);
    }

    @Override
    public List<Payee> getPayeesByAccountId(Long accountId) {
        return payeeRepository.findByAccount_AccountId(accountId);
    }

    @Override
    public Payee addPayee(Payee payee) {
        return null;
    }

    @Override
    public Payee updatePayee(Payee payee) {
        return payeeRepository.save(payee);
    }

    @Override
    public void deletePayee(int payeeId) {

        payeeRepository.deleteById(payeeId);
    }

    @Override
    public Optional<Payee> getPayeeById(int payeeId) {

        return payeeRepository.findById(payeeId);
    }

<<<<<<< HEAD
//    @Override
//    public List<Payee> findByAccount_AccountId(Long accountId) {
//        return payeeRepository.findByAccount_AccountId(accountId);
//    }


    @Override
    public List<Payee> getAllPayees() {
        // Assuming you are using some repository to fetch all payees
=======

    @Override
    public List<Payee> getAllPayees() {
>>>>>>> sharan
        return payeeRepository.findAll();
    }
}
