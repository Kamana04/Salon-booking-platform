package com.salon.service;

import com.salon.model.Salon;
import com.salon.payload.dto.SalonDTO;
import com.salon.payload.dto.UserDTO;

import java.util.List;

public interface SalonService {

    Salon createSalon(SalonDTO salonDTO, UserDTO user); // userdto -> user that is ownner of salon

    Salon updateSalon(SalonDTO salonDTO, UserDTO user, Long salonId) throws Exception;

    List<Salon> getAllSalons();

    Salon getSalonById(Long salonId) throws Exception;;

    Salon getSalonByOwnerId(Long ownerId);

    List<Salon> searchSalonByCity(String city);
}
