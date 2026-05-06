package com.salon.categoryservice.service;

import com.salon.categoryservice.dto.SalonDTO;
import com.salon.categoryservice.model.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {

    Category saveCategory(Category category, SalonDTO salonDTO);
    List<Category> getAllCategories();
    Set<Category> getAllCategoriesBySalon(Long salonId);
    Category getCategoryById(Long id) throws Exception;
    void deleteCategoryById(Long id, Long salonId) throws Exception;

}