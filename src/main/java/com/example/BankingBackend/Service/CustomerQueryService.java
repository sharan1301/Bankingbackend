package com.example.BankingBackend.Service;

import com.example.BankingBackend.Model.CustomerService;
import com.example.BankingBackend.Repository.CustomerServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class CustomerQueryService {

    @Autowired
    private CustomerServiceRepo complaintRepo;

    @Autowired
    private JavaMailSender mailSender;

    public CustomerService saveComplaint(CustomerService complaint) {
        CustomerService saved = complaintRepo.save(complaint);
        sendComplaintEmail(saved);
        return saved;
    }

    private void sendComplaintEmail(CustomerService complaint) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html><head><style>" +
                    "body { font-family: Arial, sans-serif; background-color: #eef2f3; }" +
                    ".card { background: #fff; border-radius: 10px; box-shadow: 0 2px 8px rgba(0,0,0,0.07); padding: 30px; margin: 25px auto; max-width: 600px; }" +
                    ".header { background: #d54f2f; color: #fff; padding: 12px; border-radius: 10px 10px 0 0; text-align: center; font-size: 21px; font-weight: bold; }" +
                    ".info { margin: 18px 0; font-size: 15px; color: #32414b; }" +
                    ".footer { margin-top: 23px; font-size: 13px; color: #666; text-align: center; }" +
                    ".highlight { color: #d54f2f; font-weight: bold; }" +
                    "</style></head><body>" +
                    "<div class='card'>" +
                    "<div class='header'>Customer Complaint Submitted</div>" +
                    "<div class='info'>" +
                    "<ul>" +
                    "<li><b>Product/Service:</b> " + complaint.getProductService() + "</li>" +

                    "</ul>" +
                    "<p><b>Name:</b> <span class='highlight'>" + complaint.getName() + "</span></p>" +
                    "<p><b>Account/Application No:</b> " + complaint.getAccountApplicationNumber() + "</p>" +
                    "<p><b>Comments:</b> " + complaint.getComplaintComments() + "</p>" +
                    "<p><b>Email:</b> " + complaint.getEmail() + "</p>" +
                    "<p><b>Mobile:</b> " + complaint.getMobileNumber() + "</p>" +
                    "<p><b>Telephone:</b> " + complaint.getTelephoneNumber() + "</p>" +
                    "</div>" +
                    "<div class='footer'>© 2025 SecureBank. For internal customer service use only.</div>" +
                    "</div></body></html>";

            helper.setTo("bankingofss@gmail.com");
            helper.setSubject("New Customer Complaint Submitted");
            helper.setText(htmlContent, true); // true = HTML
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send complaint email", e);
        }
    }
}

