package com.salon.salonservice.controller;

import com.salon.salonservice.model.Salon;
import com.salon.salonservice.dto.SalonDTO;
import com.salon.salonservice.dto.UserDTO;
import com.salon.salonservice.service.SalonService;
import com.salon.salonservice.service.clients.UserFeignClient;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.salon.salonservice.mapper.SalonMapper.mapToDTO;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;
    private final UserFeignClient userService;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestHeader("Authorization") String jwt,
                                                @RequestBody SalonDTO salonDTO) throws ExecutionControl.UserException {
        /*UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);*/
        UserDTO user=userService.getUserFromJwtToken(jwt).getBody();
        Salon salon = salonService.createSalon(salonDTO, user);
        SalonDTO salonMapper = mapToDTO(salon);
        return ResponseEntity.ok(salonMapper);

    }

    @PatchMapping("/{salonId}")
    public ResponseEntity<SalonDTO> updateSalon(@RequestHeader("Authorization") String jwt,
                                                @RequestBody SalonDTO salonDTO, @PathVariable Long salonId) throws Exception {
        /*UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);*/
        UserDTO user=userService.getUserFromJwtToken(jwt).getBody();
        Salon salon = salonService.updateSalon(salonDTO, user, salonId);
        SalonDTO salonMapper = mapToDTO(salon);
        return ResponseEntity.ok(salonMapper);
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getAllSalons() throws Exception {
        List<Salon> allSalons = salonService.getAllSalons();
        List<SalonDTO> salonDTOS = allSalons.stream().map((salon) ->
        {
            SalonDTO salonDTO = mapToDTO(salon);
            return salonDTO;
        }).toList();
        return ResponseEntity.ok(salonDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long id) throws Exception {
        Salon salon = salonService.getSalonById(id);
        SalonDTO salonDTO = mapToDTO(salon);
        return ResponseEntity.ok(salonDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Salon>> searchSalonByCity(@RequestParam("city") String city) {
        List<Salon> salons = salonService.searchSalonByCity(city);
        List<SalonDTO> salonDTOS = salons.stream().map((salon) ->
        {
            SalonDTO salonDTO = mapToDTO(salon);
            return salonDTO;
        }).toList();
        
        return ResponseEntity.ok(salons);
    }
    
    @GetMapping("/owner")
    public ResponseEntity<SalonDTO> getSalonByOwnerId(Long ownerId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Salon salonByOwnerId = salonService.getSalonByOwnerId(userDTO.getId());
        SalonDTO salonDTO = mapToDTO(salonByOwnerId);
        return ResponseEntity.ok(salonDTO);

    }
}
