package co.homecenter.etq.api.exception;

public class BusinessRuleException extends RuntimeException {

    private final String code;
    private final Object data;

    public BusinessRuleException(String code, String message) {
        this(code, message, null);
    }

    public BusinessRuleException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
