package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.PaymentDTO;
import lk.ijse.aad_final_project.entity.Invoice;
import lk.ijse.aad_final_project.entity.Payment;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.enums.InvoiceStatus;
import lk.ijse.aad_final_project.enums.PaymentStatus;
import lk.ijse.aad_final_project.exception.NotFoundException;
import lk.ijse.aad_final_project.exception.ValidationException;
import lk.ijse.aad_final_project.repository.InvoiceRepository;
import lk.ijse.aad_final_project.repository.PaymentRepository;
import lk.ijse.aad_final_project.repository.RentalRepository;
import lk.ijse.aad_final_project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public void savePayment(PaymentDTO paymentDTO) {

        Optional<Rental> optionalRental = rentalRepository.findById(paymentDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }
        Rental rental = optionalRental.get();

        Optional<Invoice> optionalInvoice = invoiceRepository.findByRental_RentalId(rental.getRentalId());
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found for this rental");
        }
        Invoice invoice = optionalInvoice.get();

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ValidationException("Cannot make payment for cancelled invoice");
        }

        if (paymentDTO.getAmount() == null || paymentDTO.getAmount() <= 0) {
            throw new ValidationException("Payment amount must be greater than zero");
        }

        Payment payment = new Payment();

        payment.setPaymentReference(paymentDTO.getPaymentReference());
        payment.setAmount(paymentDTO.getAmount());
        payment.setDiscount(paymentDTO.getDiscount() != null ? paymentDTO.getDiscount() : 0.0);
        payment.setPaymentDate(paymentDTO.getPaymentDate() != null ? paymentDTO.getPaymentDate() : java.time.LocalDateTime.now());
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

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment not found");
        }
        return mapToDTO(optionalPayment.get());
    }

    @Override
    public void updatePayment(PaymentDTO paymentDTO) {

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentDTO.getPaymentId());
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment not found");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(paymentDTO.getRentalId());
        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }

        Payment payment = optionalPayment.get();
        Rental rental = optionalRental.get();

        Optional<Invoice> optionalInvoice = invoiceRepository.findByRental_RentalId(rental.getRentalId());
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found for this rental");
        }

        if (paymentDTO.getAmount() == null || paymentDTO.getAmount() <= 0) {
            throw new ValidationException("Payment amount must be greater than zero");
        }

        payment.setPaymentReference(paymentDTO.getPaymentReference());
        payment.setAmount(paymentDTO.getAmount());
        payment.setDiscount(paymentDTO.getDiscount() != null ? paymentDTO.getDiscount() : 0.0);
        payment.setPaymentDate(paymentDTO.getPaymentDate() != null ? paymentDTO.getPaymentDate() : java.time.LocalDateTime.now());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus() != null ? paymentDTO.getPaymentStatus() : PaymentStatus.PAID);
        payment.setRental(rental);

        paymentRepository.save(payment);

        updateInvoiceAfterPayment(rental.getRentalId());
    }

    @Override
    public void deletePayment(Long paymentId) {

        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isEmpty()) {
            throw new NotFoundException("Payment not found");
        }

        Payment payment = optionalPayment.get();

        if (payment.getPaymentStatus() == PaymentStatus.REFUNDED) {
            throw new ValidationException("Payment is already refunded");
        }

        payment.setPaymentStatus(PaymentStatus.REFUNDED);

        paymentRepository.save(payment);

        updateInvoiceAfterPayment(payment.getRental().getRentalId());
    }

    @Override
    public List<PaymentDTO> getMyPayments(String username) {

        List<Payment> payments = paymentRepository.findPaymentsByCustomerUsername(username);
        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        for (Payment payment : payments) {
            paymentDTOList.add(mapToDTO(payment));
        }
        return paymentDTOList;
    }

    private void updateInvoiceAfterPayment(Long rentalId) {

        Optional<Invoice> optionalInvoice = invoiceRepository.findByRental_RentalId(rentalId);

        if (optionalInvoice.isEmpty()) {
            return;
        }

        Invoice invoice = optionalInvoice.get();

        List<Payment> payments = paymentRepository.findByRental_RentalId(rentalId);

        double totalPaid = 0.0;

        for (Payment payment : payments) {
            if (payment.getPaymentStatus() == PaymentStatus.PAID) {
                totalPaid += payment.getAmount();
            }
        }

        double balance = invoice.getTotalAmount() - totalPaid;

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

        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setPaymentReference(payment.getPaymentReference());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setDiscount(payment.getDiscount());
        paymentDTO.setBalance(payment.getBalance());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());

        if (payment.getRental() != null) {

            paymentDTO.setRentalId(payment.getRental().getRentalId());

            if (payment.getRental().getCustomer() != null) {

                paymentDTO.setCustomerId(payment.getRental().getCustomer().getCustomerId());

                if (payment.getRental().getCustomer().getUser() != null) {

                    String fullName = payment.getRental().getCustomer().getUser().getFirstName() + " " + payment.getRental().getCustomer().getUser().getLastName();

                    paymentDTO.setCustomerName(fullName);
                }
            }
        }
        return paymentDTO;
    }
}
