package co.homecenter.etq.api.exception;

public class LpnNotFoundException extends RuntimeException {

    private final String lpn;

    public LpnNotFoundException(String lpn) {
        super("No se encontro ETQ para el LPN indicado");
        this.lpn = lpn;
    }

    public String getLpn() {
        return lpn;
    }
}
