package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.*;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.LoanReqRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.LoanRequestNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoanReqServiceImpl implements LoanReqService {

    @Autowired
    LoanReqRepo loanReqRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;




    @Override
    public ResponseEntity<?> createLoanRequest(Map<String, Object> requestBody) {

        Long accountNumber = Long.valueOf(requestBody.get("accountNumber").toString());
        String loanTypeStr = requestBody.get("loanType").toString();
        Double loanAmount = Double.valueOf(requestBody.get("loanAmount").toString());
        Integer tenureMonths = Integer.valueOf(requestBody.get("tenureMonths").toString());

        Account account = accountRepo.findAccountByAccountNumber(accountNumber);
        if (account==null) {
            throw new RuntimeException("Account not found.");
        }
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new RuntimeException("Loan request cannot be created. Account status is " + account.getStatus());
        }
//        Account account = accountOpt.get();
        Users user = account.getUser();

        boolean pendingExists = loanReqRepo.existsByAccountAndStatus(account, LoanRequests.RequestStatus.PENDING);
        if (pendingExists) {
            throw new RuntimeException("A pending loan request already exists for this account.");
        }

        LoanRequests loanRequest = new LoanRequests();
        loanRequest.setAccount(account);
        loanRequest.setUser(user);
        loanRequest.setLoanType(LoanRequests.LoanType.valueOf(loanTypeStr));
        loanRequest.setLoanAmount(loanAmount);
        loanRequest.setTenureMonths(tenureMonths);

        loanReqRepo.save(loanRequest);
        return ResponseEntity.ok(
                Map.of("Loan request", loanRequest)
        );

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