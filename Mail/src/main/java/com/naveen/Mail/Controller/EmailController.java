package com.naveen.Mail.Controller;


import com.naveen.Mail.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/fetch-email")
    public String fetchEmail(@RequestParam String subject) {
        try {
            return emailService.fetchEmailBody(subject);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch email body: " + e.getMessage();
        }
    }
}
