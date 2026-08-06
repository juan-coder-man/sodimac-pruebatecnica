package co.homecenter.etq.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Label {

    private String etqId;
    private String lpnId;

    @JsonProperty("isPreGenerated")
    private boolean preGenerated;

    private String templateCode;
    private String zpl;

    public String getEtqId() {
        return etqId;
    }

    public void setEtqId(String etqId) {
        this.etqId = etqId;
    }

    public String getLpnId() {
        return lpnId;
    }

    public void setLpnId(String lpnId) {
        this.lpnId = lpnId;
    }

    public boolean isPreGenerated() {
        return preGenerated;
    }

    public void setPreGenerated(boolean preGenerated) {
        this.preGenerated = preGenerated;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getZpl() {
        return zpl;
    }

    public void setZpl(String zpl) {
        this.zpl = zpl;
    }
}
