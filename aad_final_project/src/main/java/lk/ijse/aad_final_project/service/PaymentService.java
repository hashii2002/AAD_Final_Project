package lk.ijse.aad_final_project.service;

import lk.ijse.aad_final_project.dto.PaymentDTO;

import java.util.List;

public interface PaymentService {

    void savePayment(PaymentDTO paymentDTO);

    List<PaymentDTO> getAllPayments();

    PaymentDTO selectPayment(Long paymentId);

    void updatePayment(PaymentDTO paymentDTO);

    void deletePayment(Long paymentId);
}
