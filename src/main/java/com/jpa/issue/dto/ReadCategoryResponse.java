package com.jpa.issue.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jpa.issue.entity.Category;
import java.util.List;

public class ReadCategoryResponse {

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ReadCategoryResponse> children;

    public ReadCategoryResponse(String name) {
        this.name = name;
    }

    public ReadCategoryResponse(Category parentCategory) {
        this.name = parentCategory.getName();
        this.children = parentCategory.getChildren()
                .stream()
                .map(child -> new ReadCategoryResponse(child.getName()))
                .toList();
    }

    public String getName() {
        return name;
    }

    public List<ReadCategoryResponse> getChildren() {
        return children;
    }
}
