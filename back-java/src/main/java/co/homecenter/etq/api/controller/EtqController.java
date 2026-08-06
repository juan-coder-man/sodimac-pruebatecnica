package co.homecenter.etq.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.homecenter.etq.api.dto.request.EtqConsultaRequest;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.EtqDetailResponse;
import co.homecenter.etq.application.service.EtqQueryService;

@RestController
@RequestMapping("/api/v1/etq")
public class EtqController {

    private final EtqQueryService etqQueryService;

    public EtqController(EtqQueryService etqQueryService) {
        this.etqQueryService = etqQueryService;
    }

    @PostMapping("/consulta")
    public ResponseEntity<ApiResponse<EtqDetailResponse>> consultar(
            @RequestBody EtqConsultaRequest request) {
        String lpn = extractLpn(request);
        if (lpn == null || lpn.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.failure("VALIDATION_ERROR", "El LPN es obligatorio"));
        }

        return etqQueryService.consultarPorLpn(lpn.trim())
                .<ResponseEntity<ApiResponse<EtqDetailResponse>>>map(detail -> ResponseEntity.ok(
                        ApiResponse.success("ETQ_FOUND", "Consulta de ETQ exitosa", detail)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.failure("LPN_NOT_FOUND", "No se encontro ETQ para el LPN indicado")));
    }

    private String extractLpn(EtqConsultaRequest request) {
        if (request == null || request.getRequest() == null) {
            return null;
        }
        return request.getRequest().getLpn();
    }
}
