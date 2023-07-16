package com.jpa.issue.dto;

import java.util.List;

public class ReadRegionResponse {

    private Long id;
    private String name;
    private List<ReadRegionResponse> children;

    public ReadRegionResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ReadRegionResponse(Long id, String name, List<ReadRegionResponse> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ReadRegionResponse> getChildren() {
        return children;
    }
}
