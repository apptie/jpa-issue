package com.jpa.issue.controller;

import com.jpa.issue.dto.CreateCategoryRequest;
import com.jpa.issue.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Long> addParentCategory(@RequestBody CreateCategoryRequest request) {
        final Long parentId = categoryService.addParentCategory(request.getName());

        return ResponseEntity.ok(parentId);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Long> addChildCategory(@RequestBody CreateCategoryRequest request, @PathVariable Long id) {
        final Long childId = categoryService.addChildCategory(request.getName(), id);

        return ResponseEntity.ok(childId);
    }
}
