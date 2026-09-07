package lk.ijse.aad_final_project.controller;

import lk.ijse.aad_final_project.constant.CommonResponse;
import lk.ijse.aad_final_project.dto.PaymentDTO;
import lk.ijse.aad_final_project.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    public CommonResponse savePayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.savePayment(paymentDTO);
        return new CommonResponse(0, "Payment Saved Successfully");
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return new CommonResponse(0, payments, "Get All Payments Successful");
    }

    @GetMapping(value = "/select/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse selectPayment(@PathVariable Long paymentId) {
        PaymentDTO paymentDTO = paymentService.selectPayment(paymentId);
        return new CommonResponse(0, paymentDTO, "Payment Selected Successfully");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updatePayment(@RequestBody PaymentDTO paymentDTO) {
        paymentService.updatePayment(paymentDTO);
        return new CommonResponse(0, "Payment Updated Successfully");
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMyPayments(Authentication authentication) {
        String username = authentication.getName();
        List<PaymentDTO> paymentDTOList = paymentService.getMyPayments(username);
        return new CommonResponse(0, paymentDTOList, "My Payments Retrieved Successfully");
    }

    @DeleteMapping(value = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deletePayment(@PathVariable Long paymentId) {
        paymentService.deletePayment(paymentId);
        return new CommonResponse(0, "Payment Refunded Successfully");
    }
}
