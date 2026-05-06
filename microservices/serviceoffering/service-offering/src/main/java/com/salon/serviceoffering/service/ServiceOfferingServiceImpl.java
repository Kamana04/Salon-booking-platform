package com.salon.serviceoffering.service;

import com.salon.serviceoffering.dto.CategoryDTO;
import com.salon.serviceoffering.dto.SalonDTO;
import com.salon.serviceoffering.dto.ServiceDTO;
import com.salon.serviceoffering.model.ServiceOffering;
import com.salon.serviceoffering.repository.ServiceOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService{

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId) {

        Set<ServiceOffering> services = serviceOfferingRepository.findBySalonId(salonId);
        if(categoryId != null) {
            services = services.stream().filter(service -> service.getCategoryId() != null
                    && service.getCategoryId()==categoryId).collect(Collectors.toSet());
        }
        return services;
    }

    @Override
    public Set<ServiceOffering> getServicesById(Set<Long> serviceIds) {
        List<ServiceOffering> services =  serviceOfferingRepository.findAllById(serviceIds);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering saveService(SalonDTO salonDTO, ServiceDTO serviceDTO, CategoryDTO categoryDTO) {
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setServiceName(serviceDTO.getServiceName());
        serviceOffering.setServiceDescription(serviceDTO.getServiceDescription());
        serviceOffering.setServicePrice(serviceDTO.getServicePrice());
        serviceOffering.setCategoryId(categoryDTO.getId());
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setDuration(serviceDTO.getDuration());
        return serviceOfferingRepository.save(serviceOffering);

    }

    @Override
    public ServiceOffering updateService(Long serviceId, ServiceOffering services) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceId).orElse(null);
        if(serviceOffering == null) {
            throw new Exception("Service does not exist with id: "+serviceId);
        }
        serviceOffering.setServiceName(services.getServiceName());
        serviceOffering.setServiceDescription(services.getServiceDescription());
        serviceOffering.setServicePrice(services.getServicePrice());
        serviceOffering.setImage(services.getImage());
        serviceOffering.setDuration(services.getDuration());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering getServiceById(Long serviceId) throws Exception {
        ServiceOffering service = serviceOfferingRepository.findById(serviceId).orElse(null);
        if(service == null) {
           throw new Exception("Service does not exist with id: "+serviceId);
        }
        return service;
    }
}
