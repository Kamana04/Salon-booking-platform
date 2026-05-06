package com.salon.controller;

import com.salon.model.Salon;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.UserDTO;
import com.salon.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.salon.mapper.SalonMapper.mapToDTO;

@RestController
@RequestMapping("/api/salons")
@RequiredArgsConstructor
public class SalonController {

    private final SalonService salonService;

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.createSalon(salonDTO, userDTO);
        SalonDTO salonMapper = mapToDTO(salon);
        return ResponseEntity.ok(salonMapper);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<SalonDTO> updateSalon(@RequestBody SalonDTO salonDTO, @PathVariable Long id) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        Salon salon = salonService.updateSalon(salonDTO, userDTO, id);
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
