package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UsersService {

    public List<Users> getAllUsers() ;

    Long userStats();

    //Long accountStats();
}
