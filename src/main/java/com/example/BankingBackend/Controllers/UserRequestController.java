package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Repository.UserRequestsRepo;
import com.example.BankingBackend.Service.UserRequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/requests")
public class UserRequestController {

    @Autowired
    UserRequestsService userReqService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequests userRequest) {
        return userReqService.register(userRequest);
    }
    @GetMapping("/registeredUsers")
    public List<UserRequests> getRegisteredUsers(){
        return userReqService.getRegisteredUsers();
    }

}
