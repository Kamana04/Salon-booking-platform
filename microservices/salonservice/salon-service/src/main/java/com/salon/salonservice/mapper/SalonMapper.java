package com.salon.salonservice.mapper;

import com.salon.salonservice.model.Salon;
import com.salon.salonservice.dto.SalonDTO;

public class SalonMapper {

    public static SalonDTO mapToDTO(Salon salon) {SalonDTO salonDTO = new SalonDTO();
       salonDTO.setId(salon.getId());
       salonDTO.setName(salon.getName());
       salonDTO.setAddress(salon.getAddress());
       salonDTO.setEmail(salon.getEmail());
       salonDTO.setCity(salon.getCity());
       salonDTO.setPhoneNumber(salon.getPhoneNumber());
       salonDTO.setOwnerId(salon.getOwnerId());
       salonDTO.setOpenTime(salon.getOpenTime());
       salonDTO.setCloseTime(salon.getCloseTime());
       salonDTO.setImages(salon.getImages());
       return salonDTO;
    }
}
