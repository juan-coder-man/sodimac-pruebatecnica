package co.homecenter.etq.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.homecenter.etq.api.dto.request.PrintRequest;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.PrintResponseData;
import co.homecenter.etq.application.service.PrintService;

@RestController
@RequestMapping("/api/v1/print")
public class PrintController {

    private final PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PrintResponseData>> imprimir(@RequestBody PrintRequest request) {
        if (request == null
                || isBlank(request.getLpn())
                || isBlank(request.getZone())
                || isBlank(request.getRequestedBy())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure(
                            "VALIDATION_ERROR",
                            "Los campos lpn, zone y requestedBy son obligatorios"));
        }

        ApiResponse<PrintResponseData> response = printService.imprimir(request);
        return ResponseEntity.ok(response);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
