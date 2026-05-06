package com.salon.serviceoffering.controller;

import com.salon.serviceoffering.model.ServiceOffering;
import com.salon.serviceoffering.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(@PathVariable Long salonId,
                                                                     @RequestParam(required = false) Long categoryId) {
        Set<ServiceOffering> allServicesBySalonId = serviceOfferingService.getAllServicesBySalonId(salonId, categoryId);
        return ResponseEntity.ok(allServicesBySalonId);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffering> getServiceById(@PathVariable Long id) throws Exception {
        ServiceOffering serviceById = serviceOfferingService.getServiceById(id);
        return ResponseEntity.ok(serviceById);

    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getServicesByIds(@PathVariable Set<Long> ids) {
        Set<ServiceOffering> allServicesIds = serviceOfferingService.getServicesById(ids);
        return ResponseEntity.ok(allServicesIds);

    }


}
