package com.salon.bookingservice.controller;

import com.salon.bookingservice.mapper.BookingMapper;
import com.salon.bookingservice.model.Booking;
import com.salon.bookingservice.model.BookingStatus;
import com.salon.bookingservice.model.PaymentMethod;
import com.salon.bookingservice.model.SalonReport;
import com.salon.bookingservice.payload.dto.*;
import com.salon.bookingservice.payload.response.PaymentLinkResponse;
import com.salon.bookingservice.service.BookingService;
import com.salon.bookingservice.service.clients.PaymentFeignClient;
import com.salon.bookingservice.service.clients.SalonFeignClient;
import com.salon.bookingservice.service.clients.ServiceOfferingFeignClient;
import com.salon.bookingservice.service.clients.UserFeignClient;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingServiceController {

    private final BookingService bookingService;
    private final UserFeignClient userService;
    private final SalonFeignClient salonService;
    private final ServiceOfferingFeignClient serviceOfferingService;
    private final PaymentFeignClient paymentService;
    private final UserFeignClient userFeignClient;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createBooking(@RequestHeader("Authorization") String jwt,
                                                             @RequestParam Long salonId,
                                                             @RequestParam PaymentMethod paymentMethod,
                                                             @RequestBody BookingRequest bookingRequest) throws Exception {
     /*   UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);
        salonDTO.setOpenTime(LocalTime.now());
        salonDTO.setCloseTime(LocalTime.now().plusHours(12));*/


        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();

        SalonDTO salon = salonService.getSalonById(salonId).getBody();
/*
        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setServicePrice(399);
        serviceDTO.setDuration(45);
        serviceDTO.setServiceName("Hair cut for men");*/

        Set<ServiceDTO> services = serviceOfferingService.getServicesByIds(bookingRequest.getServiceIds()).getBody();

        if (services.isEmpty()) {
            throw new Exception("Service not found");
        }

        //serviceDTOSet.add(serviceDTO);

        Booking createdBooking = bookingService.createBooking(bookingRequest, user, salon, services);

        PaymentLinkResponse res = paymentService.createPaymentLink(jwt, createdBooking, paymentMethod).getBody();


        return new ResponseEntity<>(res, HttpStatus.CREATED);

    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingByCustomer(@RequestHeader("Authorization") String jwt) throws ExecutionControl.UserException {

        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();

        List<Booking> booking = bookingService.getBookingByCustomer(user.getId());
        return ResponseEntity.ok(getBookingDTOs(booking));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingBySalon(@RequestHeader("Authorization") String jwt) throws Exception {

        SalonDTO salon = salonService.getSalonByOwner(jwt).getBody();

        List<Booking> bookings = bookingService.getBookingBySalon(salon.getId());

        return ResponseEntity.ok(getBookingDTOs(bookings));

    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(BookingMapper.toDTO(booking));

    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(@PathVariable Long bookingId, @RequestParam BookingStatus status) throws Exception {
        Booking updateBookingStatus = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(BookingMapper.toDTO(updateBookingStatus));

    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookSlot(@PathVariable Long salonId, @PathVariable LocalDate date) throws Exception {
        List<Booking> bookingsByDate = bookingService.getBookingsByDate(date, salonId);
        List<BookingSlotDTO> bookings = bookingsByDate.stream().map(booking -> {
            BookingSlotDTO bookingSlotDTO = new BookingSlotDTO();
            bookingSlotDTO.setStartTime(booking.getStartTime());
            bookingSlotDTO.setEndTime(booking.getCloseTime());
            return bookingSlotDTO;
        }).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(bookings);


    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(@RequestHeader("Authorization") String jwt) throws Exception {

        UserDTO user = userService.getUserFromJwtToken(jwt).getBody();

        SalonDTO salon = salonService.getSalonByOwner(jwt).getBody();

        SalonReport report = bookingService.getSalonReport(salon.getId());


        return ResponseEntity.ok(report);

    }

    private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
        return bookings.stream().map(booking -> BookingMapper.toDTO(booking)).collect(Collectors.toSet());
    }
}
