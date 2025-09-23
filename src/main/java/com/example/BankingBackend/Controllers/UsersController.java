package com.example.BankingBackend.Controllers;

import com.example.BankingBackend.Model.Users;
import com.example.BankingBackend.Service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users/auth")
@CrossOrigin(origins = {"http://127.0.0.1:5501", "http://localhost:5501"})
public class UsersController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        String rawPassword = users.getPassword();

        Optional<Users> userOpt = usersService.findByCustId(custId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid UserId or Password.");
        }
        Users user = userOpt.get();

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid UserId or Password.");
        }

        return ResponseEntity.ok("Login successful via UserId+Password.");
    }

    @PostMapping("/reset-password")
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
