package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.Account;
import com.example.BankingBackend.Model.Admin;
import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.AdminRepo;
import com.example.BankingBackend.Repository.UserRequestsRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
import com.example.BankingBackend.utils.AccountNumberGenerator;
import com.example.BankingBackend.utils.PasswordGenerator;
import com.example.BankingBackend.utils.TransactionPinGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    UserRequestsRepo userRequestsRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    EmailService emailService;
    PasswordEncoder encoder = new BCryptPasswordEncoder(12);
    String subject = "Welcome to Our Bank - Account Created Successfully";

    public List<Admin> getadmins() {
        return adminRepo.findAll();
    }

    public ResponseEntity<?> approveUser(int id) {
        Optional<UserRequests> userRequestsOpt=userRequestsRepo.findById(id);
        if(userRequestsOpt.isEmpty())
            throw new UserRequestNotFound("User Request not found");

        UserRequests userRequest=userRequestsOpt.get();
        Users user=new Users();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAadhaarNumber(userRequest.getAadhaarNumber());
        user.setPanNumber(userRequest.getPanNumber());
        user.setAccountType(userRequest.getAccountType());
        user.setOccupation(userRequest.getOccupation());
        user.setAnnualIncome(userRequest.getAnnualIncome());
        String password = PasswordGenerator.generatePassword(user.getFirstName());

        user.setPassword(encoder.encode(password));
        user.setStatus("APPROVED");
        Users savedUser = usersRepo.save(user);

        userRequest.setStatus("APPROVED");

        Account account = new Account();
        account.setUser(savedUser);

        Long accountNumber = AccountNumberGenerator.generateAccountNumber(savedUser.getUserId());
        account.setAccountNumber(accountNumber);

        account.setAccountType(Account.AccountType.valueOf(userRequest.getAccountType().toUpperCase()));

        account.setBalance(0.0);
        account.setBranchCode("ORA0005");
        account.setIfscCode("BANK000123");
        String pin=TransactionPinGenerator.generateSixDigitPin();
        account.setPin(encoder.encode(pin));
        account.setStatus(Account.AccountStatus.ACTIVE);
        account.setMinimumBalance(500.0);

        Account savedAccount = accountRepo.save(account);
        String body = "Dear Customer,\n\n"
                + "Your account has been successfully created.\n\n"
                + "Here are your account details:\n"
                + "Account Number: " + account.getAccountNumber() + "\n"
                + "Password: " + password + "\n"
                + "PIN: " + pin + "\n\n"
                + "Please keep this information safe and do not share it with anyone.\n\n"
                + "Regards,\n"
                + "Banking Support Team";
             emailService.sendEmail(user.getEmail(),subject,body);
        return ResponseEntity.ok(Map.of(
                "user", savedUser,
                "account", savedAccount
        ));
    }

    public List<UserRequests> getRegisteredUsers() {
        return userRequestsRepo.findAll();
    }

    public ResponseEntity<?> declineUser(int id) {
        Optional<UserRequests> userRequestOpt=userRequestsRepo.findById(id);
        if(userRequestOpt.isEmpty()){
            throw new UserRequestNotFound("User Request not found");
//            return ResponseEntity.badRequest().body("User request not found");
        }
        UserRequests userRequest=userRequestOpt.get();
        userRequest.setStatus("DECLINED");
        userRequestsRepo.save(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body("User request declined");


    }
}
