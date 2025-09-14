package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Admin;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AdminRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    AdminRepo adminRepo;

    public List<Admin> getadmins() {
        return adminRepo.findAll();
    }
}
