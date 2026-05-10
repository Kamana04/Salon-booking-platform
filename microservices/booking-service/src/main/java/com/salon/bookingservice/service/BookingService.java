package com.salon.bookingservice.service;

import com.salon.bookingservice.payload.dto.BookingRequest;
import com.salon.bookingservice.payload.dto.SalonDTO;
import com.salon.bookingservice.payload.dto.ServiceDTO;
import com.salon.bookingservice.payload.dto.UserDTO;
import com.salon.bookingservice.model.Booking;
import com.salon.bookingservice.model.BookingStatus;
import com.salon.bookingservice.model.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

    Booking createBooking(BookingRequest booking, UserDTO userDTO,
                          SalonDTO salonDTO, Set<ServiceDTO> serviceDTOS) throws Exception;

    List<Booking> getBookingByCustomer(Long customerId);

    List<Booking> getBookingBySalon(Long salonId);

    Booking getBookingById(Long bookingId) throws Exception;

    Booking updateBookingStatus(Long bookingId, BookingStatus bookingStatus) throws Exception;

    List<Booking> getBookingsByDate(LocalDate date,Long salonId);

    SalonReport getSalonReport(Long salonId);
}
