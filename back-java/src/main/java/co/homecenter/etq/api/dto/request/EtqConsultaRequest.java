package co.homecenter.etq.api.dto.request;

public class EtqConsultaRequest {

    private EtqConsultaPayload request;

    public EtqConsultaPayload getRequest() {
        return request;
    }

    public void setRequest(EtqConsultaPayload request) {
        this.request = request;
    }

    public static class EtqConsultaPayload {

        private String lpn;

        public String getLpn() {
            return lpn;
        }

        public void setLpn(String lpn) {
            this.lpn = lpn;
        }
    }
}
