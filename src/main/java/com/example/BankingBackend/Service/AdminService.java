package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.*;
import com.example.BankingBackend.Repository.*;
import com.example.BankingBackend.Service.Exception.LoanRequestNotFound;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
import com.example.BankingBackend.utils.AccountNumberGenerator;
import com.example.BankingBackend.utils.CustomerIdGenerator;
import com.example.BankingBackend.utils.PasswordGenerator;
import com.example.BankingBackend.utils.TransactionPinGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class AdminService {
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    UserReqRepo userRequestsRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    LoanReqRepo loanReqRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    CreditCardApplicationRepo creditCardApplicationRepo;
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    EmailService emailService;
    PasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public List<UserAccountDto> getAllUsers() {
        List<Users> users = usersRepo.findAll();
        List<UserAccountDto> result = new ArrayList<>();
            for (Users user : users) {
                Account account = accountRepo.findByUser(user); // find account linked to user
                if (account != null) {
                    boolean hasPendingLoan = loanReqRepo.existsByUserAndStatus(user, LoanRequests.RequestStatus.PENDING);
                    result.add(new UserAccountDto(
                            user.getUserId(),
                            account.getAccountId(),
                            user.getFirstName() ,
                            user.getLastName(),
                            account.getAccountNumber(),
                            account.getAccountType().name() ,
                            account.getBalance() != null ? account.getBalance() : 0.0,
                            "disabled",
                            "FROZEN".equals(account.getStatus()),
                            hasPendingLoan,
                            account.getStatus() ,
                            user.getEmail(),
                            user.getPhone()
                    ));

                }
                }
        return result;
        }


    @Transactional
    public ResponseEntity<?> approveUser(int id) {
        String password;
        String custId;
        Optional<UserRequests> userRequestsOpt = userRequestsRepo.findById(id);
        if (userRequestsOpt.isEmpty())
            throw new UserRequestNotFound("User Request not found");

        UserRequests userRequest = userRequestsOpt.get();

        // --- Check existing user by Aadhaar + PAN ---
        List<Users> existingUsers = usersRepo.findAllByAadhaarNumberAndPanNumber(
                userRequest.getAadhaarNumber(), userRequest.getPanNumber());

        Users user = new Users();

        // --- Required user fields ---
        user.setFirstName(userRequest.getFirstName() != null ? userRequest.getFirstName() : "Unknown");
        user.setLastName(userRequest.getLastName() != null ? userRequest.getLastName() : "Unknown");
        user.setEmail(userRequest.getEmail() != null ? userRequest.getEmail() : "unknown@example.com");
        user.setPhone(userRequest.getPhone() != null ? userRequest.getPhone() : "0000000000");
        user.setAadhaarNumber(userRequest.getAadhaarNumber());
        user.setPanNumber(userRequest.getPanNumber());
        user.setAccountType(userRequest.getAccountType() != null ? userRequest.getAccountType() : "Savings");
        user.setOccupation(userRequest.getOccupation());
        user.setAnnualIncome(userRequest.getAnnualIncome());
        user.setLoanRequests(null); // avoid cascade issues



        if (!existingUsers.isEmpty()) {
            Users existingUser = existingUsers.get(0);
            custId = existingUser.getCustId();
            user.setCustId(custId);
            user.setPassword(existingUser.getPassword());
        } else {
            custId = CustomerIdGenerator.generateUniqueId(user.getFirstName(), 8);
            password = PasswordGenerator.generatePassword(user.getFirstName());
            user.setCustId(custId);
            user.setPassword(encoder.encode(password));
        }

        user.setStatus("APPROVED");

        // --- Save user ---
        Users savedUser = usersRepo.save(user);

        // --- Update request status ---
        userRequest.setStatus("APPROVED");
        userRequestsRepo.save(userRequest);

        // --- Create account ---
        Account account = new Account();
        account.setUser(savedUser);
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber(savedUser.getUserId()));

        try {
            account.setAccountType(Account.AccountType.valueOf(userRequest.getAccountType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid account type: " + userRequest.getAccountType());
        }

        account.setBalance(0.0);
        account.setBranchCode("ORA0005");
        account.setIfscCode("BANK000123");

        // Generate hashed PIN
        String pin = TransactionPinGenerator.generateSixDigitPin();
        account.setPin(encoder.encode(pin)); // hashed, fits in VARCHAR2(100)

        account.setStatus(Account.AccountStatus.ACTIVE);
        account.setMinimumBalance(500.0);

        // --- Save account ---
        Account savedAccount = accountRepo.save(account);
// --- Send email AFTER DB save ---
        try {
            if (existingUsers.isEmpty()) {
                // New user → send initial password and PIN
                 password = PasswordGenerator.generatePassword(user.getFirstName());
                user.setPassword(encoder.encode(password));  // store hashed password
                usersRepo.save(user);  // update with encoded password

                String subject = "Welcome to Our Bank - Account Created Successfully";
                String body = "Dear Customer,\n\n"
                        + "Your account has been successfully created.\n\n"
                        + "Account Number: " + savedAccount.getAccountNumber() + "\n"
                        + "CustID: " + custId + "\n"
                        + "Password: " + password + "\n"      // only for new users
                        + "PIN: " + pin + "\n\n"
                        + "Please keep this information safe.\n\n"
                        + "Regards,\nBanking Support Team";
                emailService.sendEmail(user.getEmail(), subject, body);
            } else {
                // Existing user → do NOT send password
                String subject = "New Account Created Successfully";
                String body = "Dear Customer,\n\n"
                        + "A new account has been successfully created under your existing profile.\n\n"
                        + "Account Number: " + savedAccount.getAccountNumber() + "\n"
                        + "CustID: " + custId + "\n\n"
                        + "Please use your existing password to log in.\n\n"
                        + "Regards,\nBanking Support Team";
                emailService.sendEmail(user.getEmail(), subject, body);
            }
        } catch (Exception e) {
            e.printStackTrace(); // log email errors but do not rollback transaction
        }


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
        Map<String, String> response = new HashMap<>();
        response.put("message", "User request declined successfully");
        return ResponseEntity.ok(response);


    }

    public ResponseEntity<?> approveLoan(Long id) {
        LoanRequests loanRequest = loanReqRepo.findById(id)
                .orElseThrow(() -> new LoanRequestNotFound("Loan request not found for id: " + id));

        if (loanRequest.getStatus() != LoanRequests.RequestStatus.PENDING) {
            throw new IllegalStateException("Loan request is not pending.");
        }

        Loan loan = new Loan();
        loan.setUser(loanRequest.getUser());
        loan.setAccount(loanRequest.getAccount());
        loan.setLoanType(Loan.LoanType.valueOf(loanRequest.getLoanType().name())); // convert enum
        loan.setLoanAmount(loanRequest.getLoanAmount());
        loan.setTenureMonths(loanRequest.getTenureMonths());

        loan.setStartDate(LocalDate.now());
        loan.setEndDate(LocalDate.now().plusMonths(loan.getTenureMonths()));
        loan.setInterestRate(10.0); // example
        loan.setEmi(calculateEmi(loan.getLoanAmount(), loan.getInterestRate(), loan.getTenureMonths()));
        loan.setStatus(Loan.LoanStatus.ACTIVE);

        Loan savedLoan = loanRepo.save(loan);

        loanRequest.setStatus(LoanRequests.RequestStatus.APPROVED);
        loanReqRepo.save(loanRequest);

        Account account=loan.getAccount();
        account.setBalance(account.getBalance()+loan.getLoanAmount());
        accountRepo.save(account);
        String subject = "Loan Approved - Amount Credited to Your Account";
        String body = "Dear " + loan.getUser().getFirstName() + " " + loan.getUser().getLastName() + ",\n\n"
                + "Congratulations! Your loan application has been approved successfully.\n\n"
                + "Here are your loan details:\n"
                + "Loan Type: " + loanRequest.getLoanType() + "\n"
                + "Loan Amount: ₹" + loanRequest.getLoanAmount() + "\n"
                + "Tenure: " + loanRequest.getTenureMonths() + " months\n"
                + "Account Number: " + account.getAccountNumber() + "\n\n"
                + "We are pleased to inform you that the approved loan amount has been **credited immediately** to your account.\n\n"
                + "You can now access the funds through your account as usual.\n\n"
                + "If you have any questions regarding your loan, please reach out to our customer support.\n\n"
                + "Regards,\n"
                + "Banking Support Team";
        emailService.sendEmail(loan.getUser().getEmail(),subject,body);

        return ResponseEntity.ok(
                Map.of("Loan", savedLoan)
        );
    }
    private double calculateEmi(Double principal, Double rate, Integer months) {

        double monthlyRate = rate / 12 / 100;
        return principal * monthlyRate * Math.pow(1 + monthlyRate, months) / (Math.pow(1 + monthlyRate, months) - 1);
    }

    public ResponseEntity<?> declineLoan(Long id) {
        LoanRequests loanReq = loanReqRepo.findById(id)
                .orElseThrow(() -> new LoanRequestNotFound("Loan request for the id is not found"));

        loanReq.setStatus(LoanRequests.RequestStatus.REJECTED);

        LoanRequests updatedReq = loanReqRepo.save(loanReq);
        String subject = "Loan Application Status - Rejected";

        String body = "Dear " + loanReq.getUser().getFirstName() + " " + loanReq.getUser().getLastName() + ",\n\n"
                + "We regret to inform you that your loan application has not been approved at this time.\n\n"
                + "If you would like further details, please contact our support team.\n\n"
                + "Regards,\n"
                + "Banking Support Team";
        emailService.sendEmail(loanReq.getUser().getEmail(),subject,body);
        return ResponseEntity.ok(
                Map.of(
                        "LoanRequest", updatedReq
                )
        );
    }

    public ResponseEntity<?> adminProfile(Authentication authentication) {
        try {
            // Get the email from JWT token (this is what you set as subject in generateTokenWithRole)
            String email =  authentication.getName();
            // Find the admin by email
            Optional<Admin> adminOptional = adminRepo.findByEmailIgnoreCase(email);

            if (adminOptional.isEmpty()) {
                return new ResponseEntity<>("Admin not found", HttpStatus.NOT_FOUND);
            }

            Admin admin = adminOptional.get();

            // Create response object (excluding sensitive data like password)
            Map<String, Object> adminProfile = new HashMap<>();
            adminProfile.put("id", admin.getAdminId());
            adminProfile.put("WorkId",admin.getWorkId());
            adminProfile.put("firstName", admin.getFullName());
            adminProfile.put("email", admin.getEmail());
            adminProfile.put("role", "ADMIN");
            adminProfile.put("department", admin.getDepartment());
            adminProfile.put("status", "ACTIVE");

            return ResponseEntity.ok(adminProfile);

        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching admin profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<CreditCardApplication> getAllPendingCardReq() {
        return creditCardApplicationRepo.findByStatus(CreditCardApplication.ApplicationStatus.UNDER_REVIEW);
    }


}
