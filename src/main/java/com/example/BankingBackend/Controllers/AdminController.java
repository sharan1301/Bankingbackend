package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.*;
import com.example.BankingBackend.Service.AdminService;
import com.example.BankingBackend.Service.LoanReqService;
import com.example.BankingBackend.Service.UserReqService;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserReqService userRequestsService;
    @Autowired
    LoanReqService loanReqService;
    @GetMapping("/profile")
    public ResponseEntity<?> adminProfile(Authentication authentication){
        return adminService.adminProfile(authentication);
    }

    @GetMapping("/allUsers")
    public List<UserAccountDto> getAllUsers(){
            return  adminService.getAllUsers();
    }
    @GetMapping("/pendingrequests")
    public List<UserRequests> getAllPendingReq(){
        return userRequestsService.getAllPendingReq();
    }
    @GetMapping("/pendingRequests/{id}")
    public Optional<UserRequests> pendingRequestsById(@PathVariable int id){
        return userRequestsService.PendingRequestsById(id);
    }
    @PutMapping("/requests/{id}/approve")
    public ResponseEntity<?> approveUser(@PathVariable int id){
        return adminService.approveUser(id);
    }
    @PutMapping("/requests/{id}/decline")
    public ResponseEntity<?> declineUser(@PathVariable int id){
        return adminService.declineUser(id);
    }
    @GetMapping("/pendingloanrequests")
    public List<LoanRequests> getAllLoanReq(){
        return loanReqService.getPendingLoanReq();
    }
    @GetMapping("/pendingloanrequests/{userID}")
    public LoanRequests getAllLoanReq(@PathVariable int userID ){
        return  loanReqService.getPendingLoanReqByUserId(userID);
    }
    @PutMapping("/loanrequest/{id}/approve")
    public ResponseEntity<?> approveLoan(@PathVariable Long  id){
        return adminService.approveLoan(id);

    }
    @PutMapping("/loanrequest/{id}/decline")
    public ResponseEntity<?> declineLoan(@PathVariable Long  id){
        return adminService.declineLoan(id);

    }




}
