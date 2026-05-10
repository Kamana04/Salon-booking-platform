package com.salon.bookingservice.service.clients;

import com.salon.bookingservice.model.Booking;
import com.salon.bookingservice.model.PaymentMethod;
import com.salon.bookingservice.payload.response.PaymentLinkResponse;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {

    @PostMapping("/api/payments/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestHeader("Authorization") String jwt,
            @RequestBody Booking booking,
            @RequestParam PaymentMethod paymentMethod) throws ExecutionControl.UserException;
}
