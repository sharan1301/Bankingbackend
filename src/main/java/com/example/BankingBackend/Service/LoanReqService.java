package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserAccountDto;

import java.util.List;

public interface LoanReqService {
    LoanRequests createLoanRequest(Integer userId, LoanRequests input);

    List<LoanRequests> getPendingLoanReq();



    LoanRequests getPendingLoanReqByUserId(int userID);


}
