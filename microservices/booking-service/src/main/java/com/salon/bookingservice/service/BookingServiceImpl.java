package com.salon.bookingservice.service;

import com.salon.bookingservice.payload.dto.BookingRequest;
import com.salon.bookingservice.payload.dto.SalonDTO;
import com.salon.bookingservice.payload.dto.ServiceDTO;
import com.salon.bookingservice.payload.dto.UserDTO;
import com.salon.bookingservice.model.Booking;
import com.salon.bookingservice.model.BookingStatus;
import com.salon.bookingservice.model.SalonReport;
import com.salon.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest bookingRequest, UserDTO user, SalonDTO salon, Set<ServiceDTO> services) throws Exception {
        int totalDuration = services.stream().mapToInt(ServiceDTO::getDuration).sum();

        LocalDateTime bookingStartTime = bookingRequest.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        //availability check for slot
        Boolean isSlotAvailable = isTimeSlotAvailable(salon, bookingStartTime, bookingEndTime);

        //Calculate total price
        int totalPrice = services.stream().mapToInt(ServiceDTO::getServicePrice).sum();

        //service ids list
        Set<Long> idList = services.stream().map(ServiceDTO::getId).collect(Collectors.toSet());

        //create new booking
        Booking newBooking = new Booking();
        newBooking.setCustomerId(user.getId());
        newBooking.setSalonId(salon.getId());
        newBooking.setServiceIds(idList);
        newBooking.setBookingStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setCloseTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);

        return bookingRepository.save(newBooking);
    }

    //Imagine you are trying to book an appoinment at a salon. the salon has specific working hourse
    //(e.g., 9 AM to 6 PM) and you want to book an appoinment at a specific time slot.
    //They also already have some booking schedules for the day.
    //this method checks:
    //1. Whether your requested time is within the salon's working hours
    //2. Whether your requested time overlaps with any other existing bookings
    private Boolean isTimeSlotAvailable(SalonDTO salon, LocalDateTime bookingStartTime,
                                       LocalDateTime bookingEndTime) throws Exception {
        List<Booking> existingBookings = getBookingBySalon(salon.getId());
        LocalDateTime salonOpenTime = salon.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salon.getCloseTime().atDate(bookingStartTime.toLocalDate());
        //1 scenario
        if (bookingStartTime.isBefore(salonOpenTime) ||
                bookingEndTime.isAfter(salonCloseTime)) {
            throw new Exception("Booking start time must be within salon's working hours");
        }
        //2 scenario
        //iterate to all existing bookings and check all bookings time that overlaps with your requested time
        for (Booking existingBooking : existingBookings) {
            LocalDateTime existingBookingStartTime = existingBooking.getStartTime();
            LocalDateTime existingBookingEndTime = existingBooking.getCloseTime();

            if (bookingStartTime.isBefore(existingBookingEndTime) && bookingEndTime.isAfter(existingBookingStartTime)) {
              throw new Exception("Slot is not available, choose different time");
            }
            if (bookingStartTime.isEqual(existingBookingStartTime) || bookingEndTime.isEqual(existingBookingEndTime)) {
                throw new Exception("Slot is not available, choose different time");
            }
        }
        return true;

    }

    @Override
    public List<Booking> getBookingByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new Exception("Booking not found");
        }
        return booking;
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, BookingStatus bookingStatus) throws Exception {
        Booking booking = getBookingById(bookingId);
        booking.setBookingStatus(bookingStatus);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = getBookingBySalon(salonId);
        if(date == null) {
            return allBookings;
        }
        return allBookings.stream().filter(booking -> isSameDate(booking.getStartTime(), date) ||
                isSameDate(booking.getCloseTime(), date)).toList();

    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> allBookings = getBookingBySalon(salonId);
        int totalEarnings = allBookings.stream()
                .mapToInt(Booking::getTotalPrice)
                .sum();

        Integer totalBooking = allBookings.size();

        List<Booking> cancelledBookings = allBookings.stream().filter(
                booking -> booking.getBookingStatus().equals(BookingStatus.CANCELLED)
        ).toList();

        Double totalRefund = cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum();

        SalonReport salonReport = new SalonReport();
        salonReport.setSalonId(salonId);
        salonReport.setTotalBookings(totalBooking);
        salonReport.setTotalEarnings(totalEarnings);
        salonReport.setCancelledBookings(cancelledBookings.size());
        salonReport.setTotalRefund(totalRefund);

        return salonReport;
    }
}
