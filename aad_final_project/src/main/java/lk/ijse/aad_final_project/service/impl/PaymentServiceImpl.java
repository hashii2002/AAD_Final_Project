package lk.ijse.aad_final_project.service.impl;

import lk.ijse.aad_final_project.dto.PaymentDTO;
import lk.ijse.aad_final_project.entity.Payment;
import lk.ijse.aad_final_project.entity.Rental;
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

    @Override
    public void savePayment(PaymentDTO paymentDTO) {
        Optional<Rental> optionalRental = rentalRepository.findById(paymentDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }

        Rental rental = optionalRental.get();

        Payment payment = new Payment();

        payment.setPaymentReference(paymentDTO.getPaymentReference());
        payment.setAmount(paymentDTO.getAmount());
        payment.setDiscount(paymentDTO.getDiscount());
        payment.setBalance(paymentDTO.getBalance());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        payment.setRental(rental);

        paymentRepository.save(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();

        List<PaymentDTO> paymentDTOList = new ArrayList<>();

        for (Payment payment : payments) {

            PaymentDTO paymentDTO = new PaymentDTO();

            paymentDTO.setPaymentId(payment.getPaymentId());
            paymentDTO.setPaymentReference(payment.getPaymentReference());
            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setDiscount(payment.getDiscount());
            paymentDTO.setBalance(payment.getBalance());
            paymentDTO.setPaymentDate(payment.getPaymentDate());
            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDTO.setPaymentStatus(payment.getPaymentStatus());
            paymentDTO.setRentalId(payment.getRental().getRentalId());

            paymentDTOList.add(paymentDTO);
        }

        return paymentDTOList;
    }

    @Override
    public PaymentDTO selectPayment(Long paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);

        if (optionalPayment.isEmpty()) {
            throw new RuntimeException("Payment not found");
        }

        Payment payment = optionalPayment.get();

        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setPaymentReference(payment.getPaymentReference());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setDiscount(payment.getDiscount());
        paymentDTO.setBalance(payment.getBalance());
        paymentDTO.setPaymentDate(payment.getPaymentDate());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());
        paymentDTO.setRentalId(payment.getRental().getRentalId());

        return paymentDTO;
    }

    @Override
    public void updatePayment(PaymentDTO paymentDTO) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentDTO.getPaymentId());

        if (optionalPayment.isEmpty()) {
            throw new RuntimeException("Payment not found");
        }

        Optional<Rental> optionalRental = rentalRepository.findById(paymentDTO.getRentalId());

        if (optionalRental.isEmpty()) {
            throw new RuntimeException("Rental not found");
        }
        Payment payment = optionalPayment.get();
        Rental rental = optionalRental.get();

        payment.setPaymentReference(paymentDTO.getPaymentReference());
        payment.setAmount(paymentDTO.getAmount());
        payment.setDiscount(paymentDTO.getDiscount());
        payment.setBalance(paymentDTO.getBalance());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(paymentDTO.getPaymentStatus());

        payment.setRental(rental);

        paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.deleteById(paymentId);

    }
}
