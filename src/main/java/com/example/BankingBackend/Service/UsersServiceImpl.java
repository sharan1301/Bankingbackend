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

    @Autowired
    EmailService emailService;

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
    public List<Users> findByCustId(String custId){
        return usersRepo.findByCustId(custId);
    }

    @Override
    public void sendOtp(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        otpStore.put(email, otp);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Login");
        message.setText("Hello,\n\nYour OTP for login is: " + otp + "\n\nThis OTP is valid for 5 minutes.");
        mailSender.send(message);

        System.out.println("OTP sent to " + email + ": " + otp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        return otpStore.containsKey(email) && otpStore.get(email).equals(otp);
    }

    public boolean resetPassword(String custId, String newPassword) {
        List<Users> users = usersRepo.findByCustId(custId);
        if (!users.isEmpty()) {
            String hashedPassword = passwordEncoder.encode(newPassword);
            for (Users user : users) {
                user.setPassword(hashedPassword);
            }
            usersRepo.saveAll(users);
            // save all users in one batch
            Users user=users.get(0);

            String subject = "Your Password Has Been Reset";
            String body = "Dear " + user.getFirstName() + " " + user.getLastName() + ",\n\n"
                    + "Your account password has been successfully reset.\n"
                    + "Your new password is: " + newPassword + "\n\n"
                    + "Please change this password after your first login for security reasons.\n\n"
                    + "Regards,\n"
                    + "Banking Support Team";

            emailService.sendEmail(user.getEmail(), subject, body);
            return true;
        }
        return false;
    }

}
