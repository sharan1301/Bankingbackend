package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserAccountDto;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.LoanReqRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.AccountNotFound;
import com.example.BankingBackend.Service.Exception.LoanRequestNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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

        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"));

        Account requestedAccount = input.getAccount(); // Assuming input.account is populated with accountId
        if (requestedAccount == null || requestedAccount.getAccountId() == null) {
            throw new RuntimeException("No account provided in the request");
        }

        Account account = accountRepo.findById(requestedAccount.getAccountId())
                .orElseThrow(() -> new AccountNotFound("Account not found with ID " + requestedAccount.getAccountId()));

        if (!Objects.equals(account.getUser().getUserId(), userId)) {
            throw new RuntimeException("This account does not belong to the given user");
        }

        input.setUser(user);
        input.setAccount(account);

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
    public LoanRequests getPendingLoanReqByUserId(int userID) {
        LoanRequests loanRequestsOpt=loanReqRepo.findByUserIdAndStatusNative(userID,LoanRequests.RequestStatus.PENDING.name());
        if(loanRequestsOpt==null)
            throw new LoanRequestNotFound("No pending loan request for this user");
        return  loanRequestsOpt;
    }

}


