package co.homecenter.etq.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class PrintRequest {

    @NotBlank(message = "El LPN es obligatorio")
    private String lpn;

    @NotBlank(message = "La zona es obligatoria")
    private String zone;

    @NotBlank(message = "El usuario solicitante es obligatorio")
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
