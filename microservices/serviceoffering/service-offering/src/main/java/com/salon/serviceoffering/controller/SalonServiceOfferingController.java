package com.salon.serviceoffering.controller;

import com.salon.serviceoffering.dto.CategoryDTO;
import com.salon.serviceoffering.dto.SalonDTO;
import com.salon.serviceoffering.dto.ServiceDTO;
import com.salon.serviceoffering.model.ServiceOffering;
import com.salon.serviceoffering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-offering/salon-owner")
@RequiredArgsConstructor
public class SalonServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(@RequestBody ServiceDTO serviceDTO) {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(serviceDTO.getCategory());

        ServiceOffering savedService = serviceOfferingService.saveService(salonDTO, serviceDTO, categoryDTO);

        return ResponseEntity.ok(savedService);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(@PathVariable Long id,
                                                         @RequestBody ServiceOffering serviceOffering) throws Exception {
        ServiceOffering savedService = serviceOfferingService.updateService(id,serviceOffering);

        return ResponseEntity.ok(savedService);

    }
}
