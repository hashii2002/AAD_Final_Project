package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.PaymentDTO;
import lk.ijse.aad_final_project.entity.Invoice;
import lk.ijse.aad_final_project.entity.Payment;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.enums.InvoiceStatus;
import lk.ijse.aad_final_project.enums.PaymentStatus;
import lk.ijse.aad_final_project.exception.DuplicateException;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.InvoiceRepository;
import lk.ijse.aad_final_project.repository.PaymentRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public void savePayment(PaymentDTO paymentDTO) {
        validatePaymentDTO(paymentDTO);

        if (paymentDTO.getRentalId() == null) {
            throw new ValidationException("Rental ID is required");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(paymentDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }
        Rental rental = optionalRental.get();

        Optional<Invoice> optionalInvoice = invoiceRepository.findByRental_RentalId(rental.getRentalId());
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found for this rental. Generate invoice first.");
        }
        Invoice invoice = optionalInvoice.get();

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ValidationException("Cannot process payment for a cancelled invoice");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ValidationException("Invoice is already fully paid");
        }

        String reference = paymentDTO.getPaymentReference().trim();
        if (paymentRepository.existsByPaymentReference(reference)) {
            throw new DuplicateException("Payment reference already exists");
        }

        double paymentAmount = paymentDTO.getAmount();
        double currentBalance = invoice.getBalance() != null ? invoice.getBalance() : 0.0;

        if (paymentAmount > currentBalance) {
            throw new ValidationException("Payment amount exceeds the remaining invoice balance of " + currentBalance);
        }

        double remainingBalance = currentBalance - paymentAmount;

        Payment payment = new Payment();
        payment.setPaymentReference(reference);
        payment.setAmount(paymentAmount);
        payment.setDiscount(paymentDTO.getDiscount() != null ? paymentDTO.getDiscount() : 0.0);
        payment.setBalance(remainingBalance);
        payment.setPaymentDate(paymentDTO.getPaymentDate() != null ? paymentDTO.getPaymentDate() : LocalDateTime.now());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus() != null ? paymentDTO.getPaymentStatus() : PaymentStatus.PAID);
        payment.setRental(rental);

        paymentRepository.save(payment);
        updateInvoiceAfterPayment(rental.getRentalId());
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        for (Payment payment : payments) {
            paymentDTOList.add(mapToDTO(payment));
        }
        return paymentDTOList;
    }

    @Override
    public PaymentDTO selectPayment(Long paymentId) {
        if (paymentId == null) {
            throw new ValidationException("Payment ID is required");
        }

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment record not found");
        }

        return mapToDTO(optionalPayment.get());
    }

    @Override
    @Transactional
    public void updatePayment(PaymentDTO paymentDTO) {
        if (paymentDTO == null) {
            throw new ValidationException("Payment data is required");
        }
        if (paymentDTO.getPaymentId() == null) {
            throw new ValidationException("Payment ID is required for update");
        }

        validatePaymentDTO(paymentDTO);

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentDTO.getPaymentId());
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment record not found");
        }
        Payment payment = optionalPayment.get();

        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new ValidationException("Refunded payment records cannot be updated");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(paymentDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }
        Rental rental = optionalRental.get();

        String reference = paymentDTO.getPaymentReference().trim();
        if (paymentRepository.existsByPaymentReferenceAndPaymentIdNot(reference, payment.getPaymentId())) {
            throw new DuplicateException("Payment reference already exists for another payment");
        }

        payment.setPaymentReference(reference);
        payment.setAmount(paymentDTO.getAmount());
        payment.setDiscount(paymentDTO.getDiscount() != null ? paymentDTO.getDiscount() : 0.0);
        payment.setPaymentDate(paymentDTO.getPaymentDate() != null ? paymentDTO.getPaymentDate() : payment.getPaymentDate());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus() != null ? paymentDTO.getPaymentStatus() : payment.getPaymentStatus());
        payment.setRental(rental);

        paymentRepository.save(payment);
        updateInvoiceAfterPayment(rental.getRentalId());
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        if (paymentId == null) {
            throw new ValidationException("Payment ID is required");
        }

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment record not found");
        }

        Payment payment = optionalPayment.get();

        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new ValidationException("Payment has already been refunded");
        }

        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        updateInvoiceAfterPayment(payment.getRental().getRentalId());
    }

    @Override
    public List<PaymentDTO> getMyPayments(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        List<Payment> payments = paymentRepository.findPaymentsByCustomerUsername(username.trim());
        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        for (Payment payment : payments) {
            paymentDTOList.add(mapToDTO(payment));
        }
        return paymentDTOList;
    }

    private void validatePaymentDTO(PaymentDTO dto) {
        if (dto == null) {
            throw new ValidationException("Payment data is required");
        }
        if (dto.getPaymentReference() == null || dto.getPaymentReference().isBlank()) {
            throw new ValidationException("Payment reference is required");
        }
        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new ValidationException("Payment amount must be greater than zero");
        }
        if (dto.getDiscount() != null && dto.getDiscount() < 0) {
            throw new ValidationException("Discount cannot be negative");
        }
        if (dto.getPaymentMethod() == null) {
            throw new ValidationException("Payment method is required");
        }
    }

    private void updateInvoiceAfterPayment(Long rentalId) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findByRental_RentalId(rentalId);
        if (optionalInvoice.isEmpty()) {
            return;
        }

        Invoice invoice = optionalInvoice.get();

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            return;
        }

        List<Payment> payments = paymentRepository.findByRental_RentalId(rentalId);

        double totalPaid = 0.0;
        if (payments != null) {
            for (Payment p : payments) {
                if (p.getPaymentStatus() == PaymentStatus.PAID && p.getAmount() != null) {
                    totalPaid += p.getAmount();
                }
            }
        }

        double subTotal = invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0.0;
        double balance = subTotal - totalPaid;

        if (balance < 0) {
            balance = 0.0;
        }

        invoice.setBalance(balance);

        if (balance <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else if (totalPaid > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        } else {
            invoice.setStatus(InvoiceStatus.ISSUED);
        }

        invoiceRepository.save(invoice);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();

        dto.setPaymentId(payment.getPaymentId());
        dto.setPaymentReference(payment.getPaymentReference());
        dto.setAmount(payment.getAmount());
        dto.setDiscount(payment.getDiscount());
        dto.setBalance(payment.getBalance());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentStatus(payment.getPaymentStatus());

        if (payment.getRental() != null) {
            Rental rental = payment.getRental();
            dto.setRentalId(rental.getRentalId());

            if (rental.getCustomer() != null) {
                dto.setCustomerId(rental.getCustomer().getCustomerId());

                if (rental.getCustomer().getUser() != null) {
                    String firstName = rental.getCustomer().getUser().getFirstName() != null ? rental.getCustomer().getUser().getFirstName() : "";
                    String lastName = rental.getCustomer().getUser().getLastName() != null ? rental.getCustomer().getUser().getLastName() : "";
                    dto.setCustomerName((firstName + " " + lastName).trim());
                }
            }
        }

        return dto;
    }
}
