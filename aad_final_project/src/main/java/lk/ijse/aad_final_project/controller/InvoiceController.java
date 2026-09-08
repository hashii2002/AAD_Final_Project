package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.InvoiceDTO;
import lk.ijse.aad_final_project.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/invoice")
@CrossOrigin
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        invoiceService.saveInvoice(invoiceDTO);
        return new CommonResponse(0, "Invoice Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return new CommonResponse(0, invoices, "Get All Invoices Successful");
    }

    @GetMapping(value = "/select/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectInvoice(@PathVariable Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.selectInvoice(invoiceId);
        return new CommonResponse(0, invoiceDTO, "Invoice Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        invoiceService.updateInvoice(invoiceDTO);
        return new CommonResponse(0, "Invoice Updated Successfully");
    }

    @DeleteMapping(value = "/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return new CommonResponse(0, "Invoice Cancelled Successfully");
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMyInvoices(Authentication authentication) {
        String username = authentication.getName();
        List<InvoiceDTO> invoiceDTOList = invoiceService.getMyInvoices(username);
        return new CommonResponse(0, invoiceDTOList, "My Invoices Retrieved Successfully");
    }
}
