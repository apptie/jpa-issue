package com.jpa.issue.dto;

import java.util.List;

public class ReadRegionResponse {

    private String name;
    private List<ReadRegionResponse> children;

    public ReadRegionResponse(String name) {
        this.name = name;
    }

    public ReadRegionResponse(String name, List<ReadRegionResponse> children) {
        this.name = name;
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public List<ReadRegionResponse> getChildren() {
        return children;
    }
}
