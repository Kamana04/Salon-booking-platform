package com.salon.paymentservice.controller;

import com.razorpay.RazorpayException;
import com.salon.paymentservice.domain.PaymentMethod;
import com.salon.paymentservice.dto.BookingDTO;
import com.salon.paymentservice.dto.UserDTO;
import com.salon.paymentservice.model.PaymentOrder;
import com.salon.paymentservice.payload.response.PaymentLinkResponse;
import com.salon.paymentservice.service.PaymentService;
import com.salon.paymentservice.service.clients.UserFeignClient;
import com.stripe.exception.StripeException;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserFeignClient userService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@RequestBody BookingDTO booking,
                                                                 @RequestHeader("Authorization") String jwt,
                                                                 @RequestParam PaymentMethod paymentMethod) throws StripeException, RazorpayException, ExecutionControl.UserException {

        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();

        PaymentLinkResponse paymentLinkResponse = paymentService
                .createOrder(user, booking, paymentMethod);

        return ResponseEntity.ok(paymentLinkResponse);

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
