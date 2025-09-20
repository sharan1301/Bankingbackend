package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.LoanReqRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.AccountNotFound;
import com.example.BankingBackend.Service.Exception.LoanRequestNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanReqServiceImpl implements LoanReqService {

    @Autowired
    LoanReqRepo loanReqRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;

    @Override
    public LoanRequests createLoanRequest(Integer userId, LoanRequests input) {
        Optional<Users> userOpt = usersRepo.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }

      Optional<Account> accountOpt = accountRepo.findByUserId(userId);
        if(accountOpt.get()==null){
            throw  new AccountNotFound("The account for the user does not exist");
        }
        Users user = userOpt.get();
        input.setUser(user);

        if (input.getStatus() == null) {
            input.setStatus(LoanRequests.RequestStatus.PENDING);
        }
        return loanReqRepo.save(input);
    }

    @Override
    public List<LoanRequests> getPendingLoanReq() {
        return loanReqRepo.findByStatus(LoanRequests.RequestStatus.valueOf(String.valueOf(LoanRequests.RequestStatus.PENDING)));

    }

    @Override
    public LoanRequests getPendingLoanReqById(Long id) {
        Optional<LoanRequests> loanRequestsOpt=loanReqRepo.findById(id);
        if(loanRequestsOpt.isEmpty())
            throw new LoanRequestNotFound("Loan request for the id is not found");
        return loanRequestsOpt.get();
    }
}