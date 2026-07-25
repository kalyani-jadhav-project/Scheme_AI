package com.krushimitra.app.dto.request;

public class ApplicationRequest {
    private Long schemeId;
    private String remarks;

    public Long getSchemeId() { return schemeId; }
    public void setSchemeId(Long schemeId) { this.schemeId = schemeId; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
}
