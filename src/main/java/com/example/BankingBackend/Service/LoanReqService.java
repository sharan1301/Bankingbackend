package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.LoanRequests;

import java.util.List;

public interface LoanReqService {
    LoanRequests createLoanRequest(Integer userId, LoanRequests input);

    List<LoanRequests> getPendingLoanReq();

    LoanRequests getPendingLoanReqById(Long id);
}
