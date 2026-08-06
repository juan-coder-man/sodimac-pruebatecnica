package co.homecenter.etq.api.dto.response;

public class PrintResponseData {

    private String requestId;
    private String etqId;
    private String lpnId;
    private String zone;
    private String eventType;
    private String result;
    private String reason;
    private String zpl;
    private String printedAt;
    private String reprintReason;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

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

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getZpl() {
        return zpl;
    }

    public void setZpl(String zpl) {
        this.zpl = zpl;
    }

    public String getPrintedAt() {
        return printedAt;
    }

    public void setPrintedAt(String printedAt) {
        this.printedAt = printedAt;
    }

    public String getReprintReason() {
        return reprintReason;
    }

    public void setReprintReason(String reprintReason) {
        this.reprintReason = reprintReason;
    }
}
