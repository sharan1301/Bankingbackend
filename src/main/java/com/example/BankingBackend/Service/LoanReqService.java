package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserAccountDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LoanReqService {
    //LoanRequests createLoanRequest(Integer userId, LoanRequests input);

    ResponseEntity<?> createLoanRequest(Map<String, Object> requestBody);

    List<LoanRequests> getPendingLoanReq();



    LoanRequests getPendingLoanReqByUserId(int userID);


}
