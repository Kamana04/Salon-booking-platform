package com.salon.paymentservice.service.clients;


import com.salon.paymentservice.domain.PaymentMethod;
import com.salon.paymentservice.payload.response.PaymentLinkResponse;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PAYMENT-SERVICE")
public interface PaymentFeignClient {

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestHeader("Authorization") String jwt,
            @RequestParam Long bookingId,
            @RequestParam PaymentMethod paymentMethod) throws ExecutionControl.UserException;
}
