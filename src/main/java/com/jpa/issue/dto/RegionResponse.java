package com.jpa.issue.dto;

import com.jpa.issue.entity.Region;

public class RegionResponse {

    private String y_coor;
    private String full_addr;
    private String x_coor;
    private String addr_name;
    private String cd;

    public RegionResponse() {
    }

    public Region toEntity() {
        return new Region(addr_name);
    }

    public String getY_coor() {
        return y_coor;
    }

    public String getFull_addr() {
        return full_addr;
    }

    public String getX_coor() {
        return x_coor;
    }

    public String getAddr_name() {
        return addr_name;
    }

    public String getCd() {
        return cd;
    }

    @Override
    public String toString() {
        return "RegionResponse{" +
                "y_coor='" + y_coor + '\'' +
                ", full_addr='" + full_addr + '\'' +
                ", x_coor='" + x_coor + '\'' +
                ", addr_name='" + addr_name + '\'' +
                ", cd='" + cd + '\'' +
                '}';
    }
}
