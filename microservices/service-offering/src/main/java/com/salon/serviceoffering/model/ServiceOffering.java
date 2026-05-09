package com.salon.serviceoffering.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ServiceOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private String serviceDescription;

    @Column(nullable = false)
    private int servicePrice;

    @Column(nullable = false)
    private int duration;

    //for which salon this service is offered
    @Column(nullable = false)
    private Long salonId;

    //Under which category this service is offered
    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false)
    private String image;


}
