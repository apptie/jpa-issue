package com.jpa.issue.service;

import com.jpa.issue.entity.Category;
import com.jpa.issue.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Long addParentCategory(String name) {
        final Category category = new Category(name);

        categoryRepository.save(category);

        return category.getId();
    }

    public Long addChildCategory(String name, Long parentId) {
        final Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 카테고리가 존재하지 않습니다."));

        final Category child = new Category(name);

        parent.initChild(child);

        return child.getId();
    }

    public Category findParentCategoryWithJpql(Long parentId) {
        return categoryRepository.findParentWithAllChildrenById(parentId);
    }

    public Category findParentCategoryWithEntityGraph(Long parentId) {
        return categoryRepository.findChildrenIdWithParentById(parentId);
    }
}
