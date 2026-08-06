package co.homecenter.etq.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.homecenter.etq.api.dto.response.ApiError;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.HistoryItemResponse;
import co.homecenter.etq.application.service.HistoryService;
import co.homecenter.etq.domain.enums.PrintResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/print")
@Tag(name = "History", description = "Historial de impresiones")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/history")
    @Operation(summary = "Consultar historial de impresiones")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "Historial consultado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Parametro result invalido")
    })
    public ResponseEntity<ApiResponse<List<HistoryItemResponse>>> history(
            @RequestParam(required = false) String lpn,
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String result) {
        PrintResult printResult = null;
        if (result != null && !result.isBlank()) {
            try {
                printResult = PrintResult.valueOf(result.trim().toUpperCase());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.failure(
                                "VALIDATION_ERROR",
                                "El parametro result debe ser EXITOSO o RECHAZADO",
                                List.of(new ApiError(
                                        "result",
                                        "INVALID",
                                        "Valores permitidos: EXITOSO, RECHAZADO"))));
            }
        }

        List<HistoryItemResponse> items = historyService.listar(lpn, zone, printResult);
        return ResponseEntity.ok(ApiResponse.success(
                "HISTORY_OK",
                "Historial de impresiones consultado",
                items));
    }
}
