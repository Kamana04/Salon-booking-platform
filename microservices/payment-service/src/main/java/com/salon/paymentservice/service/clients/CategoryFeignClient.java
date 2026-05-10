package com.salon.paymentservice.service.clients;

import com.salon.paymentservice.dto.CategoryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryFeignClient {

    @GetMapping("/api/categories/{id}")
    ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id);
}
