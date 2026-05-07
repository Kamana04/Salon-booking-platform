package com.salon.bookingservice.dto;

import com.salon.bookingservice.model.BookingStatus;
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

    private BookingStatus bookingStatus = BookingStatus.PENDING;

    private int totalPrice;
}
