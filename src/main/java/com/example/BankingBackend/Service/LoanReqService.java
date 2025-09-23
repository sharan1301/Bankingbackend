package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserAccountDto;

import java.util.List;
import java.util.Map;

public interface LoanReqService {
    void createLoanRequest(Map<String, Object> requestBody);

    List<LoanRequests> getPendingLoanReq();



    LoanRequests getPendingLoanReqByUserId(int userID);


}
