package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.InvoiceDTO;

import java.util.List;

public interface InvoiceService {
    void saveInvoice(InvoiceDTO invoiceDTO);

    List<InvoiceDTO> getAllInvoices();

    InvoiceDTO selectInvoice(Long invoiceId);

    void updateInvoice(InvoiceDTO invoiceDTO);

    void deleteInvoice(Long invoiceId);
}
