package com.salon.categoryservice.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category { // category means hair color, hair smoothening etc, waxing etc

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String image;

    @Column(nullable = false)
    private Long salonId; // from which salon you want to create category

}
