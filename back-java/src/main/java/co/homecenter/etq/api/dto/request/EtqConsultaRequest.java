package co.homecenter.etq.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EtqConsultaRequest {

    @NotNull(message = "El objeto request es obligatorio")
    @Valid
    private EtqConsultaPayload request;

    public EtqConsultaPayload getRequest() {
        return request;
    }

    public void setRequest(EtqConsultaPayload request) {
        this.request = request;
    }

    public static class EtqConsultaPayload {

        @NotBlank(message = "El LPN es obligatorio")
        private String lpn;

        public String getLpn() {
            return lpn;
        }

        public void setLpn(String lpn) {
            this.lpn = lpn;
        }
    }
}
