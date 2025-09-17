package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.UserRequestsRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
import com.example.BankingBackend.utils.PasswordGenerator;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class UserRequestsService {
    @Autowired
    UserRequestsRepo userReqRepo;
    @Autowired
    UsersRepo usersRepo;
    public ResponseEntity<?> register( UserRequests userRequests) {
        if (userRequests.getStatus() == null) {
            userRequests.setStatus("PENDING");
        }
        if (userRequests.getRequestDate() == null) {
            userRequests.setRequestDate(LocalDate.now());
        }
        UserRequests saved=userReqRepo.save(userRequests);
        return  ResponseEntity.ok(saved);
    }

    public List<UserRequests> getAllPendingReq() {
        return userReqRepo.findByStatus("PENDING");
    }

    public Optional<UserRequests> PendingRequestsById(int id) {
        return userReqRepo.findById(id);
    }

    public ResponseEntity<?> approveUser(int id) {
        Optional<UserRequests> userRequestsOpt=userReqRepo.findById(id);
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
        user.setPassword(password);
        user.setStatus("APPROVED");
        Users savedUser = usersRepo.save(user);

        userRequest.setStatus("APPROVED");
        userReqRepo.save(userRequest);

        return ResponseEntity.ok(savedUser);
    }

    public List<UserRequests> getRegisteredUsers() {
        return userReqRepo.findAll();
    }

    public ResponseEntity<?> declineUser(int id) {
        Optional<UserRequests> userRequestOpt=userReqRepo.findById(id);
        if(userRequestOpt.isEmpty()){
            throw new UserRequestNotFound("User Request not found");
//            return ResponseEntity.badRequest().body("User request not found");
        }
        UserRequests userRequest=userRequestOpt.get();
        userRequest.setStatus("DECLINED");
        userReqRepo.save(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body("User request declined");


    }
}
