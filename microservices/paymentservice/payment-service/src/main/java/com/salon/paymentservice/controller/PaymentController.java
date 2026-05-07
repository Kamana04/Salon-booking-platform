package com.salon.paymentservice.controller;

import com.razorpay.RazorpayException;
import com.salon.paymentservice.domain.PaymentMethod;
import com.salon.paymentservice.dto.BookingDTO;
import com.salon.paymentservice.dto.UserDTO;
import com.salon.paymentservice.model.PaymentOrder;
import com.salon.paymentservice.payload.response.PaymentLinkResponse;
import com.salon.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking,
                                                                 @RequestParam PaymentMethod paymentMethod) throws StripeException, RazorpayException {
        UserDTO user = new UserDTO();
        user.setFullName("Kamana");
        user.setEmail("kamana04@gmail.com");
        user.setId(1L);

        PaymentLinkResponse response = paymentService.createOrder(user, booking, paymentMethod);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long paymentOrderId) throws Exception {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentOrderId);
        return ResponseEntity.ok(paymentOrder);

    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> processPayment(@RequestParam String paymentId,
                                                       @RequestParam String paymentLinkId) throws Exception {
        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        Boolean proceedPayment = paymentService.proceedPayment(paymentOrder, paymentId, paymentLinkId);
        return ResponseEntity.ok(proceedPayment);

    }

}
