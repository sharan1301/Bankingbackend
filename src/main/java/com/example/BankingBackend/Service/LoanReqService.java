package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.LoanRequests;

public interface LoanReqService {
    LoanRequests createLoanRequest(Integer userId, LoanRequests input);
}
