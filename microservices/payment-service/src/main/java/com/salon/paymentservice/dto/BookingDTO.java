package com.salon.paymentservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;

    private Long salonId;

    private Long customerId;

    private LocalDateTime startTime;

    private LocalDateTime closeTime;

    private Set<Long> serviceIds;

    private int totalPrice;
}
