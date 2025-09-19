package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class UserReqController {

    @Autowired
    UserReqService userReqService;

    }
}
