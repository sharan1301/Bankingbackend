package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UsersServiceImpl implements UsersService{
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    private final Map<String, String> otpStore = new HashMap<>();

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

    @Override
    public Optional<Users> findByEmail(String email) {
        return usersRepo.findByEmail(email);
    }

    @Override
    public Optional<Users> findByCustId(String custId){
        return usersRepo.findByCustId(custId);
    }

    @Override
    public void sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Login");
        message.setText("Your OTP for login is: " + otp + "\n\nThis OTP is valid for 5 minutes.\n\nPlease do not share this otp with anyone.\n\nIf request was not submitted by you, immediately contact our support team.");
        mailSender.send(message);

        System.out.println("OTP sent to " + email + ": " + otp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        return otpStore.containsKey(email) && otpStore.get(email).equals(otp);
    }

    public boolean resetPassword(String custId, String newPassword) {
        Optional<Users> userOpt = usersRepo.findByCustId(custId);
        if (userOpt.isPresent()) {
            Users user = userOpt.get();
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(hashedPassword);
            usersRepo.save(user);
            return true;
        }
        return false;
    }
}
