package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Repository.UserReqRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserReqServiceImpl implements UserReqService{

    @Autowired
    UserReqRepo userReqRepo;

    @Override
    public UserRequests createRequest(UserRequests request){
        request.setStatus("PENDING");
        return userReqRepo.save(request);
    }
}