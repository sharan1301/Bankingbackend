
package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Payee;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.PayeeRepo;
import com.example.BankingBackend.Service.PayeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayeeServiceImpl implements PayeeService {


//    private final PayeeRepository payeeRepository;

    @Autowired

    PayeeRepo payeeRepository;

    @Autowired
    AccountRepo accountRepository;



    @Override
    public Payee addPayee(Payee payee, Long accountId) {
        // Fetch the Account by the accountId from the URL
        Optional<Account> account = accountRepository.findById(accountId);

        if (!account.isPresent()) {
            throw new IllegalArgumentException("Account with ID " + accountId + " does not exist.");
        }

        // Set the Account to the Payee object
        payee.setAccount(account.get());

        // Save and return the Payee
        payee.setAccount(account.get());

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
    public boolean deletePayee(int payeeId) {
        if (payeeRepository.existsById(payeeId)) {
            payeeRepository.deleteById(payeeId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Payee> getPayeeById(int payeeId) {

        return payeeRepository.findById(payeeId);
    }

    @Override
    public List<Payee> getAllPayees() {
        return payeeRepository.findAll();
    }
}
