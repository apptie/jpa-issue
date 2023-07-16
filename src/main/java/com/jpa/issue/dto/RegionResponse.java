package com.jpa.issue.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jpa.issue.entity.Region;

public class RegionResponse {

    @JsonProperty("y_coor")
    private String latitude;

    @JsonProperty("full_addr")
    private String fullAddress;

    @JsonProperty("x_coor")
    private String longitude;

    @JsonProperty("addr_name")
    private String regionName;

    private String cd;

    public RegionResponse() {
    }

    public RegionResponse(String regionName, String cd) {
        this.regionName = regionName;
        this.cd = cd;
    }

    public Region toEntity() {
        return new Region(cd, regionName);
    }

    public String getLatitude() {
        return latitude;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getRegionName() {
        return regionName;
    }

    public String getCd() {
        return cd;
    }
}
