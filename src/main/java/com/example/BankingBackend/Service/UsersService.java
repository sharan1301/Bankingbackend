package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersService {
    @Autowired
    UsersRepo usersRepo;
    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }
}
