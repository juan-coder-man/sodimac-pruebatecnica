package co.homecenter.etq.domain.rule;

public class ValidationOutcome {

    private final boolean allowed;
    private final String code;
    private final String reason;
    private final boolean reprint;

    public ValidationOutcome(boolean allowed, String code, String reason, boolean reprint) {
        this.allowed = allowed;
        this.code = code;
        this.reason = reason;
        this.reprint = reprint;
    }

    public static ValidationOutcome ok(boolean reprint) {
        return new ValidationOutcome(true, null, null, reprint);
    }

    public static ValidationOutcome rejected(String code, String reason) {
        return new ValidationOutcome(false, code, reason, false);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public boolean isReprint() {
        return reprint;
    }
}
