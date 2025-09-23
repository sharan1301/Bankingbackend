package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users/auth")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        Optional<Users> user = usersService.findByEmail(email);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Email not found, please register.");
        }
        usersService.sendOtp(email);
        return ResponseEntity.ok("OTP sent successfully.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = usersService.verifyOtp(email, otp);
        if (!verified) {
            return ResponseEntity.badRequest().body("Invalid OTP.");
        }
        return ResponseEntity.ok("Login successful via OTP.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users) {
        String custId = users.getCustId();
        String password = users.getPassword();

        Optional<Users> user = usersService.findByCustIdAndPassword(custId, password);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid UserId or Password.");
        }
        return ResponseEntity.ok("Login successful via UserId+Password.");
    }

}
