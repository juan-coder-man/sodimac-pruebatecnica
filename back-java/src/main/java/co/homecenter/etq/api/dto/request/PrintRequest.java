package co.homecenter.etq.api.dto.request;

public class PrintRequest {

    private String lpn;
    private String zone;
    private String requestedBy;
    private String reprintReason;

    public String getLpn() {
        return lpn;
    }

    public void setLpn(String lpn) {
        this.lpn = lpn;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getReprintReason() {
        return reprintReason;
    }

    public void setReprintReason(String reprintReason) {
        this.reprintReason = reprintReason;
    }
}
