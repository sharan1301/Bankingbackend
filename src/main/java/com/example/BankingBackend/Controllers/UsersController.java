package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    UsersService usersService;
    @GetMapping("/allUsers")
    public List<Users> getAllUsers(){
        return usersService.getAllUsers();
    }
}
