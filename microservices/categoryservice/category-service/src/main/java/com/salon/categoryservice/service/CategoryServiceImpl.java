package com.salon.categoryservice.service;

import com.salon.categoryservice.dto.SalonDTO;
import com.salon.categoryservice.model.Category;
import com.salon.categoryservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category saveCategory(Category category, SalonDTO salonDTO) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setImage(category.getImage());
        newCategory.setSalonId(salonDTO.getId());
        return categoryRepository.save(newCategory);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Set<Category> getAllCategoriesBySalon(Long salonId) {
        return categoryRepository.findBySalonId(salonId);
    }

    @Override
    public Category getCategoryById(Long id) throws Exception {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new Exception("Category does not exist with id: "+id);
        }
        return category;
    }

    @Override
    public void deleteCategoryById(Long id, Long salonId) throws Exception {
        Category category = getCategoryById(id);
        if (!category.getSalonId().equals(salonId)) { // user can delete only his category not others category
            throw new Exception("You don't have permission to delete this category");
        }
        categoryRepository.deleteById(id);

    }
}
