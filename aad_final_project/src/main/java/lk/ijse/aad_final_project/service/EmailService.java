package lk.ijse.aad_final_project.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);

    void sendRentalReminderEmail(String to, String customerName, String vehicleNumber, String endDate);

}
