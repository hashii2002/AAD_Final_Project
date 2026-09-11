package lk.ijse.aad_final_project.controller;

import jakarta.validation.Valid;
import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.InvoiceDTO;
import lk.ijse.aad_final_project.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CommonResponse> saveInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        invoiceService.saveInvoice(invoiceDTO);
        CommonResponse response = new CommonResponse(0, "Invoice Saved Successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        CommonResponse response = new CommonResponse(0, invoices, "Get All Invoices Successful");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectInvoice(@PathVariable Long invoiceId) {
        InvoiceDTO invoiceDTO = invoiceService.selectInvoice(invoiceId);
        CommonResponse response = new CommonResponse(0, invoiceDTO, "Invoice Selected Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updateInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        invoiceService.updateInvoice(invoiceDTO);
        CommonResponse response = new CommonResponse(0, "Invoice Updated Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        CommonResponse response = new CommonResponse(0, "Invoice Cancelled Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyInvoices(Authentication authentication) {
        String username = authentication.getName();
        List<InvoiceDTO> invoiceDTOList = invoiceService.getMyInvoices(username);
        CommonResponse response = new CommonResponse(0, invoiceDTOList, "My Invoices Retrieved Successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
