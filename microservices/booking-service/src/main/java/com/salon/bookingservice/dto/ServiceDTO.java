package com.salon.bookingservice.dto;

import lombok.Data;

@Data
public class ServiceDTO {

    private Long id;

    private String serviceName;

    private String serviceDescription;

    private int servicePrice;

    private int duration;

    private Long category;

    private String image;
}
