package lk.ijse.aad_final_project.scheduler;

import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.entity.User;
import lk.ijse.aad_final_project.enums.RentalStatus;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RentalReminderScheduler {

    private final RentalRepository rentalRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Colombo")
    //@Scheduled(cron = "*/10 * * * * *", zone = "Asia/Colombo")
    @Transactional(readOnly = true)
    public void sendRentalReminderEmails() {

        log.info("Rental reminder scheduler started");

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDateTime startDate = tomorrow.atStartOfDay();
        LocalDateTime endDate = tomorrow.atTime(LocalTime.MAX);

        List<RentalStatus> excludedStatuses = List.of(RentalStatus.CANCELLED, RentalStatus.COMPLETED);

        List<Rental> rentals = rentalRepository.findRentalsForReminder(startDate, endDate, excludedStatuses);

        for (Rental rental : rentals) {

            try {

                if (rental.getCustomer() == null || rental.getCustomer().getUser() == null) {
                    log.warn("Customer/User not found for rental ID: {}", rental.getRentalId());
                    continue;
                }

                User user = rental.getCustomer().getUser();

                if (user.getEmail() == null || user.getEmail().isBlank()) {
                    log.warn("Customer email not found for rental ID: {}", rental.getRentalId());
                    continue;
                }

                String customerName = user.getFirstName();
                String customerEmail = user.getEmail();
                String vehicleNumber = (rental.getVehicle() != null && rental.getVehicle().getVehicleNo() != null) ? rental.getVehicle().getVehicleNo() : "N/A";
                String rentalEndDate = rental.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                emailService.sendRentalReminderEmail(customerEmail, customerName, vehicleNumber, rentalEndDate);

                log.info("Rental reminder email sent for rental ID: {} to {}", rental.getRentalId(), customerEmail);

            } catch (Exception e) {
                log.error("Failed to send rental reminder for rental ID: {}", rental.getRentalId(), e);
            }
        }

        log.info("Rental reminder scheduler completed");
    }
}