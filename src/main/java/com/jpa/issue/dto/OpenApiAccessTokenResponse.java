package com.jpa.issue.dto;

import java.util.Map;

public class OpenApiAccessTokenResponse {

    private String id;
    private Map<String, String> result;
    private String errMsg;
    private String errCd;
    private String trId;

    public OpenApiAccessTokenResponse() {
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getResult() {
        return result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getErrCd() {
        return errCd;
    }

    public String getTrId() {
        return trId;
    }
}
