package com.jpa.issue.dto;

import java.util.List;

public class OpenApiRegionResponse {

    private String id;
    private List<RegionResponse> result;

    public OpenApiRegionResponse() {
    }

    public String getId() {
        return id;
    }

    public List<RegionResponse> getResult() {
        return result;
    }
}
