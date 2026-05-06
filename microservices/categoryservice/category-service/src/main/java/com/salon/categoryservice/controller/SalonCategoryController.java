package com.salon.categoryservice.controller;

import com.salon.categoryservice.dto.SalonDTO;
import com.salon.categoryservice.model.Category;
import com.salon.categoryservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories/salon-owner") // owner-specific
@RequiredArgsConstructor
public class SalonCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);
        Category saveCategory = categoryService.saveCategory(category, salonDTO);

        return ResponseEntity.ok(saveCategory);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) throws Exception {
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);
        categoryService.deleteCategoryById(id, salonDTO.getId());
        return ResponseEntity.ok("Category deleted successfully");

    }
}
