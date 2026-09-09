package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.InvoiceDTO;
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
import lk.ijse.aad_final_project.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void saveInvoice(InvoiceDTO invoiceDTO) {

        Optional<Rental> optionalRental = rentalRepository.findById(invoiceDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new NotFoundException("Rental not found");
        }
        Rental rental = optionalRental.get();

        // One invoice per rental
        if (invoiceRepository.findByRental_RentalId(rental.getRentalId()).isPresent()) {
            throw new DuplicateException("Invoice already exists for this rental");
        }

        Invoice invoice = new Invoice();

        invoice.setIssueDate(invoiceDTO.getIssueDate() != null ? invoiceDTO.getIssueDate() : java.time.LocalDateTime.now());
        double subTotal = rental.getTotalAmount();
        double discount = invoiceDTO.getDiscount() != null ? invoiceDTO.getDiscount() : 0.0;

        if (discount < 0) {
            throw new ValidationException("Discount cannot be negative");
        }

        if (discount > subTotal) {
            throw new ValidationException("Discount cannot be greater than subtotal");
        }

        double totalAmount = subTotal - discount;

        invoice.setSubTotal(subTotal);
        invoice.setDiscount(discount);
        invoice.setTotalAmount(totalAmount);

        double totalPaid = calculateTotalPaid(rental.getRentalId());
        double balance = totalAmount - totalPaid;

        if (balance < 0) {
            balance = 0.0;
        }

        invoice.setBalance(balance);
        invoice.setStatus(calculateInvoiceStatus(totalPaid, balance));
        invoice.setRental(rental);

        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();

        for (Invoice invoice : invoices) {
            invoiceDTOList.add(mapToDTO(invoice));
        }
        return invoiceDTOList;
    }

    @Override
    public InvoiceDTO selectInvoice(Long invoiceId) {

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);

        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found");
        }

        return mapToDTO(optionalInvoice.get());
    }

    @Override
    public void updateInvoice(InvoiceDTO invoiceDTO) {

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceDTO.getInvoiceId());
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found");
        }
        Invoice invoice = optionalInvoice.get();

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ValidationException("Cancelled invoice cannot be updated");
        }

        Rental rental = invoice.getRental();
        double subTotal = rental.getTotalAmount();
        double discount = invoiceDTO.getDiscount() != null ? invoiceDTO.getDiscount() : 0.0;

        if (discount < 0) {
            throw new ValidationException("Discount cannot be negative");
        }
        if (discount > subTotal) {
            throw new ValidationException("Discount cannot be greater than subtotal");
        }

        double totalAmount = subTotal - discount;
        double totalPaid = calculateTotalPaid(rental.getRentalId());
        double balance = totalAmount - totalPaid;

        if (balance < 0) {
            balance = 0.0;
        }

        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setSubTotal(subTotal);
        invoice.setDiscount(discount);
        invoice.setTotalAmount(totalAmount);
        invoice.setBalance(balance);
        invoice.setStatus(calculateInvoiceStatus(totalPaid, balance));

        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long invoiceId) {

        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isEmpty()) {
            throw new NotFoundException("Invoice not found");
        }

        Invoice invoice = optionalInvoice.get();

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new ValidationException("Invoice is already cancelled");
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);

        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> getMyInvoices(String username) {

        List<Invoice> invoices = invoiceRepository.findInvoicesByCustomerUsername(username);
        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();

        for (Invoice invoice : invoices) {
            invoiceDTOList.add(mapToDTO(invoice));
        }

        return invoiceDTOList;
    }

    private double calculateTotalPaid(Long rentalId) {

        List<Payment> payments = paymentRepository.findByRental_RentalId(rentalId);

        double totalPaid = 0.0;

        for (Payment payment : payments) {

            if (payment.getPaymentStatus() == PaymentStatus.PAID) {
                totalPaid += payment.getAmount();
            }
        }
        return totalPaid;
    }

    private InvoiceStatus calculateInvoiceStatus(double totalPaid, double balance) {

        if (balance <= 0) {
            return InvoiceStatus.PAID;
        }
        if (totalPaid > 0) {
            return InvoiceStatus.PARTIALLY_PAID;
        }
        return InvoiceStatus.ISSUED;
    }

    private InvoiceDTO mapToDTO(Invoice invoice) {

        InvoiceDTO dto = new InvoiceDTO();

        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setSubTotal(invoice.getSubTotal());
        dto.setDiscount(invoice.getDiscount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setBalance(invoice.getBalance());
        dto.setStatus(invoice.getStatus());

        if (invoice.getRental() != null) {
            Rental rental = invoice.getRental();
            dto.setRentalId(rental.getRentalId());

            if (rental.getCustomer() != null) {
                dto.setCustomerId(rental.getCustomer().getCustomerId());

                if (rental.getCustomer().getUser() != null) {
                    String fullName = rental.getCustomer().getUser().getFirstName() + " " + rental.getCustomer().getUser().getLastName();
                    dto.setCustomerName(fullName);
                }
            }

            if (rental.getPayments() != null && !rental.getPayments().isEmpty()) {
                Payment payment = rental.getPayments().get(0);
                dto.setPaymentId(payment.getPaymentId());
            }
        }
        return dto;
    }
}
