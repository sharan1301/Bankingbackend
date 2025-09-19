package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.UserRequestsRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class UserReqServiceImpl implements UserReqService {
    @Autowired
    UserRequestsRepo userReqRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;
    public ResponseEntity<?> register( UserRequests userRequests) {
        if (userRequests.getStatus() == null) {
            userRequests.setStatus("PENDING");
        }
        if (userRequests.getRequestDate() == null) {
            userRequests.setRequestDate(LocalDate.now());
        }
        UserRequests saved=userReqRepo.save(userRequests);
        return  ResponseEntity.ok(saved);
    }

    public List<UserRequests> getAllPendingReq() {
        return userReqRepo.findByStatus("PENDING");
    }

    public Optional<UserRequests> PendingRequestsById(int id) {
        Optional<UserRequests> userRequestsOpt=userReqRepo.findById(id);
        if(userRequestsOpt.isEmpty())
            throw new UserRequestNotFound("User Request not found");
        return userRequestsOpt;
    }


}
