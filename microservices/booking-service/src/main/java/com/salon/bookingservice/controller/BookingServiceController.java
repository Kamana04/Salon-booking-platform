package com.salon.bookingservice.controller;

import com.salon.bookingservice.dto.*;
import com.salon.bookingservice.mapper.BookingMapper;
import com.salon.bookingservice.model.Booking;
import com.salon.bookingservice.model.BookingStatus;
import com.salon.bookingservice.model.SalonReport;
import com.salon.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingServiceController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);
        salonDTO.setOpenTime(LocalTime.now());
        salonDTO.setCloseTime(LocalTime.now().plusHours(12));

        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setServicePrice(399);
        serviceDTO.setDuration(45);
        serviceDTO.setServiceName("Hair cut for men");

        serviceDTOSet.add(serviceDTO);

        Booking booking = bookingService.createBooking(bookingRequest, userDTO, salonDTO, serviceDTOSet);
        return ResponseEntity.ok(booking);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingByCustomer() {

        List<Booking> booking = bookingService.getBookingByCustomer(1L);
        return ResponseEntity.ok(getBookingDTOs(booking));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingBySalon() {

        List<Booking> booking = bookingService.getBookingBySalon(1L);
        return ResponseEntity.ok(getBookingDTOs(booking));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(BookingMapper.toDTO(booking));

    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long bookingId,
                                                          @RequestParam BookingStatus status) throws Exception {
        Booking updateBookingStatus = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(BookingMapper.toDTO(updateBookingStatus));

    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookSlot(@PathVariable Long salonId, @PathVariable LocalDate date) throws Exception {
        List<Booking> bookingsByDate = bookingService.getBookingsByDate(date, salonId);
        List<BookingSlotDTO> bookings = bookingsByDate.stream()
                .map(booking -> {
                    BookingSlotDTO bookingSlotDTO = new BookingSlotDTO();
                    bookingSlotDTO.setStartTime(booking.getStartTime());
                    bookingSlotDTO.setEndTime(booking.getCloseTime());
                    return bookingSlotDTO;
                }).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(bookings);


    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport() throws Exception {
        SalonReport salonReport = bookingService.getSalonReport(1L);
        return ResponseEntity.ok(salonReport);

    }

    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream()
                .map(booking -> BookingMapper.toDTO(booking))
                .collect(Collectors.toSet());
    }
}
