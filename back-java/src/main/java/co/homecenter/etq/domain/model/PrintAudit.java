package co.homecenter.etq.domain.model;

import java.time.Instant;

import co.homecenter.etq.domain.enums.EventType;
import co.homecenter.etq.domain.enums.PrintResult;

public class PrintAudit {

    private String id;
    private String requestId;
    private String etqId;
    private String lpnId;
    private String zone;
    private String requestedBy;
    private Instant printedAt;
    private PrintResult result;
    private EventType eventType;
    private String reason;
    private String reprintReason;
    private String zpl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Instant getPrintedAt() {
        return printedAt;
    }

    public void setPrintedAt(Instant printedAt) {
        this.printedAt = printedAt;
    }

    public PrintResult getResult() {
        return result;
    }

    public void setResult(PrintResult result) {
        this.result = result;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReprintReason() {
        return reprintReason;
    }

    public void setReprintReason(String reprintReason) {
        this.reprintReason = reprintReason;
    }

    public String getZpl() {
        return zpl;
    }

    public void setZpl(String zpl) {
        this.zpl = zpl;
    }
}
