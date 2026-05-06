package com.salon.serviceoffering.service;

import com.salon.serviceoffering.dto.CategoryDTO;
import com.salon.serviceoffering.dto.SalonDTO;
import com.salon.serviceoffering.dto.ServiceDTO;
import com.salon.serviceoffering.model.ServiceOffering;

import java.util.Set;

public interface ServiceOfferingService {

    Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId);

    Set<ServiceOffering> getServicesById(Set<Long> serviceIds);

    ServiceOffering saveService(SalonDTO salonDTO, ServiceDTO serviceDTO,
                                        CategoryDTO categoryDTO);

    ServiceOffering updateService(Long serviceId, ServiceOffering serviceOfferingSalonDTO) throws Exception;

    ServiceOffering getServiceById(Long serviceId) throws Exception;

}
