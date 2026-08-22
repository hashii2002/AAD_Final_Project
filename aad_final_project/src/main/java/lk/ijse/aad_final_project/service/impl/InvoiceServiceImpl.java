package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.InvoiceDTO;
import lk.ijse.aad_final_project.entity.Invoice;
import lk.ijse.aad_final_project.entity.Rental;
import lk.ijse.aad_final_project.repository.InvoiceRepository;
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

    @Override
    public void saveInvoice(InvoiceDTO invoiceDTO) {
        Optional<Rental> optionalRental = rentalRepository.findById(invoiceDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Rental rental = optionalRental.get();

        Invoice invoice = new Invoice();
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setSubTotal(invoiceDTO.getSubTotal());
        invoice.setDiscount(invoiceDTO.getDiscount());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        invoice.setBalance(invoiceDTO.getBalance());
        invoice.setStatus(invoiceDTO.getStatus());
        invoice.setRental(rental);

        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();

        for (Invoice invoice : invoices) {
            InvoiceDTO dto = new InvoiceDTO();
            dto.setInvoiceId(invoice.getInvoiceId());
            dto.setIssueDate(invoice.getIssueDate());
            dto.setSubTotal(invoice.getSubTotal());
            dto.setDiscount(invoice.getDiscount());
            dto.setTotalAmount(invoice.getTotalAmount());
            dto.setBalance(invoice.getBalance());
            dto.setStatus(invoice.getStatus());
            dto.setRentalId(invoice.getRental().getRentalId());

            invoiceDTOList.add(dto);
        }

        return invoiceDTOList;
    }

    @Override
    public InvoiceDTO selectInvoice(Long invoiceId) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);

        if (optionalInvoice.isEmpty()) {
            throw new RuntimeException("Invoice not found");
        }

        Invoice invoice = optionalInvoice.get();

        InvoiceDTO dto = new InvoiceDTO();
        dto.setInvoiceId(invoice.getInvoiceId());
        dto.setIssueDate(invoice.getIssueDate());
        dto.setSubTotal(invoice.getSubTotal());
        dto.setDiscount(invoice.getDiscount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setBalance(invoice.getBalance());
        dto.setStatus(invoice.getStatus());
        dto.setRentalId(invoice.getRental().getRentalId());

        return dto;
    }

    @Override
    public void updateInvoice(InvoiceDTO invoiceDTO) {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceDTO.getInvoiceId());

        if (optionalInvoice.isEmpty()) {
            throw new RuntimeException("Invoice not found");
        }

        Invoice invoice = optionalInvoice.get();
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setSubTotal(invoiceDTO.getSubTotal());
        invoice.setDiscount(invoiceDTO.getDiscount());
        invoice.setTotalAmount(invoiceDTO.getTotalAmount());
        invoice.setBalance(invoiceDTO.getBalance());
        invoice.setStatus(invoiceDTO.getStatus());

        invoiceRepository.save(invoice);
    }

    @Override
    public void deleteInvoice(Long invoiceId) {
        if (!invoiceRepository.existsById(invoiceId)) {
            throw new RuntimeException("Invoice not found");
        }

        invoiceRepository.deleteById(invoiceId);
    }
}
