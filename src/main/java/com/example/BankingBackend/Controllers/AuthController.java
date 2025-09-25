package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Admin;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AdminRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Requests.LoginRequestAdmin;
import com.example.BankingBackend.Requests.LoginRequestUser;
import com.example.BankingBackend.Service.UsersService;
import com.example.BankingBackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
@RestController
public class AuthController {
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    UsersService usersService;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PasswordEncoder pwdEncoder;
    @PostMapping("/auth/adminlogin")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequestAdmin adminrequest){
        String workId = adminrequest.getWorkId();
        String password = adminrequest.getPassword();

        var adminOptional = adminRepo.findByWorkIdIgnoreCase(workId);
        if(adminOptional.isEmpty()){
            return new ResponseEntity<>("Admin not registered", HttpStatus.UNAUTHORIZED);
        }

        Admin admin = adminOptional.get();
        if(!pwdEncoder.matches(password, admin.getPassword())){
            return new ResponseEntity<>("Invalid Admin", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateTokenWithRole(admin.getEmail(), "ROLE_ADMIN");

        // ✅ NEW: Create admin data object (exclude sensitive info like password)
        Map<String, Object> adminData = Map.of(
                "id", admin.getAdminId(),
                "workId", admin.getWorkId(),
                "name", admin.getFullName(),
                "email", admin.getEmail(),
                "department", admin.getDepartment() != null ? admin.getDepartment() : "",
                "role", "ADMIN",
                "lastLoginTime", new Date() // Current login time
        );

        // ✅ MODIFIED: Return token, role, and admin data
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", "ADMIN",
                "admin", adminData  // This is the key addition
        ));
    }
    @PostMapping("/auth/userlogin")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequestUser userrequest){
        String custId = userrequest.getCustId();
        String password = userrequest.getPassword();

        List<Users> userOptional = usersRepo.findByCustId(custId);
        if(userOptional.isEmpty()){
            return new ResponseEntity<>("User not registered", HttpStatus.UNAUTHORIZED);
        }

        Users user = userOptional.get(0);
        if(!pwdEncoder.matches(password, user.getPassword())){
            return new ResponseEntity<>("Invalid User", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateTokenWithRole(user.getEmail(), "ROLE_USER");

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getUserId());
        userData.put("custId", user.getCustId());
        userData.put("email", user.getEmail());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("phone", user.getPhone());
        userData.put("aadhaarNumber", user.getAadhaarNumber());
        userData.put("panNumber", user.getPanNumber());
        userData.put("accountType", user.getAccountType());
        userData.put("occupation", user.getOccupation());
        userData.put("annualIncome", user.getAnnualIncome());
        userData.put("status", user.getStatus());
        userData.put("lastLoginTime", new Date());


        // ✅ MODIFIED: Return token, role, and user data
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", "USER",
                "user", userData  // This is the key addition
        ));
    }
    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String custId,
            @RequestParam String newPassword) {
        boolean updated = usersService.resetPassword(custId, newPassword);
        if (updated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid username.");
        }
    }


}
