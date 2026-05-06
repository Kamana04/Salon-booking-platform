package com.salon.salonservice.service;

import com.salon.salonservice.model.Salon;
import com.salon.salonservice.dto.SalonDTO;
import com.salon.salonservice.dto.UserDTO;
import com.salon.salonservice.repository.SalonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService{

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO req, UserDTO user) {
        Salon salon = new Salon();
        salon.setName(req.getName());
        salon.setAddress(req.getAddress());
        salon.setEmail(req.getEmail());
        salon.setCity(req.getCity());
        salon.setPhoneNumber(req.getPhoneNumber());
        salon.setOwnerId(user.getId());
        salon.setOpenTime(req.getOpenTime());
        salon.setCloseTime(req.getCloseTime());
        salon.setImages(req.getImages());
        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDTO salonDTO, UserDTO user, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);
        if(existingSalon != null && salonDTO.getOwnerId().equals(user.getId())) { //only owner of the salon can update the salon
            existingSalon.setCity(salonDTO.getCity());
            existingSalon.setAddress(salonDTO.getAddress());
            existingSalon.setName(salonDTO.getName());
            existingSalon.setEmail(salonDTO.getEmail());
            existingSalon.setPhoneNumber(salonDTO.getPhoneNumber());
            existingSalon.setOpenTime(salonDTO.getOpenTime());
            existingSalon.setCloseTime(salonDTO.getCloseTime());
            existingSalon.setImages(salonDTO.getImages());
            existingSalon.setOwnerId(user.getId());
            return salonRepository.save(existingSalon);
        }
        throw new Exception("Salon not exists");
    }

    @Override
    public List<Salon> getAllSalons() {
       return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
        Salon salon = salonRepository.findById(salonId).orElse(null);
        if(salon == null) {
            throw new Exception("salon does not exist");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCity(String city) {
        return salonRepository.searchSalons(city);
    }
}
