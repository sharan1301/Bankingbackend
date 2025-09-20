package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-requests")
public class UserReqController {

    @Autowired
    UserReqService userReqService;
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody UserRequests request) {
        return userReqService.createRequest(request);
       // return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);
    }
}
