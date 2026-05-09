package com.salon.bookingservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long salonId;

    private Long customerId;

    private LocalDateTime startTime;

    private LocalDateTime closeTime;

    @ElementCollection
    private Set<Long> serviceIds;  // some user can book facial or haircut or massage

    private BookingStatus bookingStatus = BookingStatus.PENDING; //BY DEFAULT PENDING

    private int totalPrice;
}
