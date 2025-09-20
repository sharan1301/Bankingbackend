package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Admin;
import com.example.BankingBackend.Model.LoanRequests;
import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Service.AdminService;
import com.example.BankingBackend.Service.LoanReqService;
import com.example.BankingBackend.Service.UserReqService;
import com.example.BankingBackend.Service.UserReqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    UserReqService userRequestsService;
    @Autowired
    LoanReqService loanReqService;
    @GetMapping("/getAdmins")
    public List<Admin> getAdmins(){
            return  adminService.getadmins();
    }
    @GetMapping("/pendingrequests")
    public List<UserRequests> getAllPendingReq(){
        return userRequestsService.getAllPendingReq();
    }
    @GetMapping("/pendingRequests/{id}")
    public Optional<UserRequests> pendingRequestsById(@PathVariable int id){
        return userRequestsService.PendingRequestsById(id);
    }
    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveUser(@PathVariable int id){
        return adminService.approveUser(id);
    }
    @PutMapping("/{id}/decline")
    public ResponseEntity<?> declineUser(@PathVariable int id){
        return adminService.declineUser(id);
    }
    @GetMapping("/pendingloanrequests")
    public List<LoanRequests> getAllLoanReq(){
        return loanReqService.getPendingLoanReq();
    }
    @GetMapping("/pendingloanrequests/{id}")
    public LoanRequests getAllLoanReq(@PathVariable Long  id){
        return loanReqService.getPendingLoanReqById(id);
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
