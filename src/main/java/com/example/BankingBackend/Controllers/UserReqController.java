package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requests")
public class UserReqController {

    @Autowired
    UserReqService userReqService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequests userRequest) {
        return userReqService.register(userRequest);
    }


}
