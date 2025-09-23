package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.UserRequests;
import com.example.BankingBackend.Repository.AccountRepo;
import com.example.BankingBackend.Repository.UserReqRepo;
import com.example.BankingBackend.Repository.UsersRepo;
import com.example.BankingBackend.Service.Exception.UserRequestNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserReqServiceImpl implements UserReqService {
    @Autowired
    UserReqRepo userReqRepo;
    @Autowired
    UsersRepo usersRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    @Override
    public ResponseEntity<?> createRequest(UserRequests request) {
        Optional<UserRequests> existing = userReqRepo.findByAadhaarNumberAndStatus(request.getAadhaarNumber(), "PENDING");
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("You already have a request under verification.");
        }

        try {
            UserRequests savedRequest = userReqRepo.save(request);
            sendConfirmationEmail(savedRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRequest);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating request: " + e.getMessage());
        }
    }

    private void sendConfirmationEmail(UserRequests request) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "  <style>" +
                    "    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }" +
                    "    .card { background: white; max-width: 600px; margin: auto; border-radius: 10px; padding: 20px; " +
                    "            box-shadow: 0 4px 8px rgba(0,0,0,0.1); }" +
                    "    .header { background-color: #003366; color: white; padding: 15px; border-radius: 10px 10px 0 0; text-align: center; font-size: 20px; font-weight: bold; }" +
                    "    .content { padding: 20px; font-size: 16px; line-height: 1.6; color: #333; }" +
                    "    .footer { margin-top: 20px; font-size: 14px; color: #777; text-align: center; }" +
                    "    .highlight { color: #003366; font-weight: bold; }" +
                    "  </style>" +
                    "</head>" +
                    "<body>" +
                    "  <div class='card'>" +
                    "    <div class='header'>SecureBank - Account Opening Request</div>" +
                    "    <div class='content'>" +
                    "      <p>Dear <span class='highlight'>" + request.getFirstName() + " " + request.getLastName() + "</span>,</p>" +
                    "      <p>Thank you for submitting your account opening request with <b>SecureBank</b>. \uD83D\uDE4F</p>" +
                    "      <p>Your request is now under <b>verification</b>. Our team will review your documents and reach out to you shortly.</p>" +
                    "      <p><b>Details Submitted:</b></p>" +
                    "      <ul>" +
                    "        <li><b>Aadhaar:</b> " + request.getAadhaarNumber() + "</li>" +
                    "        <li><b>PAN:</b> " + request.getPanNumber() + "</li>" +
                    "        <li><b>Account Type:</b> " + request.getAccountType() + "</li>" +
                    "      </ul>" +
                    "      <p>Meanwhile, if you have any questions, contact our customer service.</p>" +
                    "    </div>" +
                    "    <div class='footer'>© 2025 SecureBank. All Rights Reserved.</div>" +
                    "  </div>" +
                    "</body>" +
                    "</html>";

            helper.setTo(request.getEmail());
            helper.setSubject("SecureBank - Account Request Submitted");
            helper.setText(htmlContent, true); // true = HTML
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public List<UserRequests> getAllPendingReq() {
        return userReqRepo.findByStatus("PENDING");
    }


    @Override
    public Optional<UserRequests> PendingRequestsById(int id) {
        Optional<UserRequests> userRequestsOpt=userReqRepo.findById(id);
        if(userRequestsOpt.isEmpty())
            throw new UserRequestNotFound("User Request not found");
        return userRequestsOpt;
    }

    @Override
    public Long pendingReqStats() {
        return userReqRepo.countByStatus("PENDING");
    }
}
