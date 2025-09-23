package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Service.AccountService;
import com.example.BankingBackend.Service.UserReqService;
import com.example.BankingBackend.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
@RequestMapping("/admin")
public class AdminDashboardController {
    @Autowired
    UsersService usersService;
    @Autowired
    UserReqService userReqService;
   @Autowired
   AccountService accountService;
    @GetMapping("/dashboard/users-stats")
    public Map<String, Long> userStats(){
        Map<String, Long> response = new HashMap<>();
        response.put("totalUsers", usersService.userStats());
        return response;
    }
    @GetMapping("/dashboard/accounts-stats")
    public Map<String, Long> accountStats(){

        return accountService.accountStats();
    }
    @GetMapping("/dashboard/pending-requests")
    public Map<String, Long> pendingrequestsStats(){
        Map<String, Long> response = new HashMap<>();
        response.put("pendingCount", userReqService.pendingReqStats());
        return response;
    }
    @GetMapping("dashboard/revenue-stats")
    public Map<String,Long> revenueStats(){
        Long val= 0L;
        Map<String, Long> response = new HashMap<>();
        response.put("monthlyRevenue",val);
        return response;
    }

}
