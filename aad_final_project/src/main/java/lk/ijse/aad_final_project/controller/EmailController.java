package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestParam String email) {

        emailService.sendEmail(
                email,
                "Vehicle Rental System - Test Email",
                "This is a test email from the Vehicle Rental & Fleet Management System."
        );

        return ResponseEntity.ok("Test email sent successfully");
    }
}
