package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Admin;
import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Repository.AdminRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Requests.LoginRequestAdmin;
import com.example.BankingBackend.Requests.LoginRequestUser;
import com.example.BankingBackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthController {
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PasswordEncoder pwdEncoder;
    @PostMapping("/auth/adminlogin")
    public ResponseEntity<?> adminLogin(@RequestBody LoginRequestAdmin adminrequest){
              String workId=adminrequest.getWorkId();
              String password=adminrequest.getPassword();
              var adminOptional=adminRepo.findByWorkIdIgnoreCase(workId);
              if(adminOptional.isEmpty()){
                  return new ResponseEntity<>("Admin not registered",HttpStatus.UNAUTHORIZED);
              }
              Admin admin=adminOptional.get();
              if(!pwdEncoder.matches(password, admin.getPassword())){
                  return new ResponseEntity<>("Invalid Admin",HttpStatus.UNAUTHORIZED);
              }
              String token=jwtUtil.generateTokenWithRole(admin.getEmail(),"ROLE_ADMIN");
              return ResponseEntity.ok(Map.of("token",token,"role","ADMIN"));
    }
    @PostMapping("/auth/userlogin")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequestUser userrequest){
        String userName=userrequest.getUserName();
        String password=userrequest.getPassword();
        var userOptional=usersRepo.findByPassword(password);
        if(userOptional.isEmpty()){
            return new ResponseEntity<>("User not registered",HttpStatus.UNAUTHORIZED);
        }
        Users user=userOptional.get();
        if(!password.equals(user.getPassword())){
            return new ResponseEntity<>("Invalid User",HttpStatus.UNAUTHORIZED);
        }
        String token=jwtUtil.generateTokenWithRole(user.getEmail(),"ROLE_USER");
        return ResponseEntity.ok(Map.of("token",token,"role","USER"));
    }

}
