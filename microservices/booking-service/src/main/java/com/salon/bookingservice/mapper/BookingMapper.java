package com.salon.bookingservice.mapper;

import com.salon.bookingservice.payload.dto.BookingDTO;
import com.salon.bookingservice.model.Booking;

public class BookingMapper {

    public static BookingDTO toDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setCloseTime(booking.getCloseTime());
        bookingDTO.setBookingStatus(booking.getBookingStatus());
        bookingDTO.setServiceIds(booking.getServiceIds());

        return bookingDTO;

    }
}
