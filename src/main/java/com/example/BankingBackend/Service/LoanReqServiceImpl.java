package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.LoanReqRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoanReqServiceImpl implements LoanReqService {

    @Autowired
    LoanReqRepo loanReqRepo;
    @Autowired
    UsersRepo usersRepo;


    @Override
    public LoanRequests createLoanRequest(Integer userId, LoanRequests input) {
        Optional<Users> userOpt = usersRepo.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }

        Users user = userOpt.get();
        input.setUser(user);

        if (input.getStatus() == null) {
            input.setStatus(LoanRequests.RequestStatus.PENDING);
        }
        return loanReqRepo.save(input);
    }
}
