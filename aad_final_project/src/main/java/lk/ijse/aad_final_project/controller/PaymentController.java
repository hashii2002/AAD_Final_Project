package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.PaymentDTO;
import lk.ijse.aad_final_project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/payment")
@CrossOrigin
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> savePayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.savePayment(paymentDTO);
        CommonResponse response = new CommonResponse(0, "Payment Saved Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        CommonResponse response = new CommonResponse(0, payments, "Get All Payments Successful");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/select/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> selectPayment(@PathVariable Long paymentId) {
        PaymentDTO paymentDTO = paymentService.selectPayment(paymentId);
        CommonResponse response = new CommonResponse(0, paymentDTO, "Payment Selected Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> updatePayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.updatePayment(paymentDTO);
        CommonResponse response = new CommonResponse(0, "Payment Updated Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> getMyPayments(Authentication authentication) {
        String username = authentication.getName();
        List<PaymentDTO> paymentDTOList = paymentService.getMyPayments(username);
        CommonResponse response = new CommonResponse(0, paymentDTOList, "My Payments Retrieved Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse> deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        CommonResponse response = new CommonResponse(0, "Payment Refunded Successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
