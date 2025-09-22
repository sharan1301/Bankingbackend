package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersServiceImpl implements UsersService{
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;
    @Override
    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }
//    @Override
//    public Long accountStats() {
//        return accountRepo.countByStatusAndUserIsNotNull(Account.AccountStatus.ACTIVE);
//    }

    @Override
    public Long userStats() {
        return Long.valueOf(usersRepo.count());
    }
}
