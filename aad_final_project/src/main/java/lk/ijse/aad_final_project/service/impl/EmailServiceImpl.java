package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            log.info("Email sent successfully to: {}", to);

        } catch (Exception e) {

            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendRentalReminderEmail(String to, String customerName, String vehicleNo, String endDate) {

        String subject = "Vehicle Rental Reminder - Rental Ending Soon";

        String body =
                "Dear " + customerName + ",\n\n" +

                        "This is a friendly reminder that your vehicle rental " +
                        "is coming to an end soon.\n\n" +

                        "Vehicle Number : " + vehicleNo + "\n" +
                        "Rental End Date : " + endDate + "\n\n" +

                        "Please make sure to return the vehicle on or before " +
                        "the rental end date.\n\n" +

                        "If you need to extend your rental period, " +
                        "please contact our Vehicle Rental & Fleet Management System " +
                        "in advance.\n\n" +

                        "Thank you for choosing our service.\n\n" +

                        "Best Regards,\n" +
                        "Vehicle Rental & Fleet Management System";

        sendEmail(to, subject, body);
    }
}
