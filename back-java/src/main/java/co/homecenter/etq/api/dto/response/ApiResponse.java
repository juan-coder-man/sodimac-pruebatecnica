package co.homecenter.etq.api.dto.response;

import java.util.Collections;
import java.util.List;

public class ApiResponse<T> {

    private boolean success;
    private String code;
    private String message;
    private T data;
    private List<ApiError> errors;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, String code, String message, T data, List<ApiError> errors) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors != null ? errors : Collections.emptyList();
    }

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(true, code, message, data, Collections.emptyList());
    }

    public static <T> ApiResponse<T> failure(String code, String message) {
        return new ApiResponse<>(false, code, message, null, Collections.emptyList());
    }

    public static <T> ApiResponse<T> failure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data, Collections.emptyList());
    }

    public static <T> ApiResponse<T> failure(String code, String message, List<ApiError> errors) {
        return new ApiResponse<>(false, code, message, null, errors != null ? errors : Collections.emptyList());
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<ApiError> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }
}
