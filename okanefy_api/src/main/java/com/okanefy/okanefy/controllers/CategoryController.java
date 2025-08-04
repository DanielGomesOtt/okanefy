package com.okanefy.okanefy.controllers;

import com.okanefy.okanefy.dto.category.CategoriesListDTO;
import com.okanefy.okanefy.dto.category.CreatedCategoryDTO;
import com.okanefy.okanefy.dto.category.NewCategoryDTO;
import com.okanefy.okanefy.dto.category.UpdateCategoryDTO;
import com.okanefy.okanefy.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public ResponseEntity<CreatedCategoryDTO> save(@RequestBody @Valid NewCategoryDTO category) {
        CreatedCategoryDTO createdCategory= service.save(category);
        return ResponseEntity.status(201).body(createdCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<CreatedCategoryDTO> update(@RequestBody @Valid UpdateCategoryDTO data) {
        CreatedCategoryDTO updatedCategory = service.update(data);
        return ResponseEntity.ok(updatedCategory);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CategoriesListDTO>> findAll(@PathVariable Long userId, @RequestParam(required = false) String name, @RequestParam(required = false) String type) {
        List<CategoriesListDTO> categories = service.findAll(userId, name, type);
        return ResponseEntity.ok(categories);
    }
}
