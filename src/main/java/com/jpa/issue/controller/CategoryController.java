package com.jpa.issue.controller;

import com.jpa.issue.dto.CreateCategoryRequest;
import com.jpa.issue.dto.ReadCategoryResponse;
import com.jpa.issue.entity.Category;
import com.jpa.issue.service.CategoryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/jpql/{parentId}")
    public ResponseEntity<List<ReadCategoryResponse>> findParentCategoryWithJpql(@PathVariable Long parentId) {
        final List<Category> categories = categoryService.findParentCategoryWithJpql(parentId);

        final List<ReadCategoryResponse> responses = categories.stream()
                .map(ReadCategoryResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/graph/{parentId}")
    public ResponseEntity<List<ReadCategoryResponse>> findParentCategoryWithEntityGraph(@PathVariable Long parentId) {
        final List<Category> categories = categoryService.findParentCategoryWithEntityGraph(parentId);

        final List<ReadCategoryResponse> responses = categories.stream()
                .map(ReadCategoryResponse::new)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
