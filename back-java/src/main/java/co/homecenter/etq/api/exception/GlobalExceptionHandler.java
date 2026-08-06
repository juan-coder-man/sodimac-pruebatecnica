package co.homecenter.etq.api.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.homecenter.etq.api.dto.response.ApiError;
import co.homecenter.etq.api.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toApiError)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(
                        "VALIDATION_ERROR",
                        "La solicitud contiene datos invalidos",
                        errors));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException ex) {
        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toApiError)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(
                        "VALIDATION_ERROR",
                        "La solicitud contiene datos invalidos",
                        errors));
    }

    @ExceptionHandler(LpnNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleLpnNotFound(LpnNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure("LPN_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessRule(BusinessRuleException ex) {
        return ResponseEntity.ok(ApiResponse.failure(ex.getCode(), ex.getMessage(), ex.getData()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.failure(
                        "VALIDATION_ERROR",
                        "El cuerpo de la solicitud no es un JSON valido"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "INTERNAL_ERROR",
                        "Ocurrio un error interno al procesar la solicitud"));
    }

    private ApiError toApiError(FieldError fieldError) {
        String code = fieldError.getCode() != null
                ? fieldError.getCode().toUpperCase()
                : "INVALID";
        return new ApiError(fieldError.getField(), code, fieldError.getDefaultMessage());
    }
}
