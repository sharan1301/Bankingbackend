package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UsersService {

    public List<Users> getAllUsers() ;


    Long userStats();

    Optional<Users> findByEmail(String email);
    Optional<Users> findByCustId (String CustId);
    void sendOtp(String email);
    boolean verifyOtp(String email, String otp);
    boolean resetPassword(String custId, String password);

    //Long accountStats();

}
