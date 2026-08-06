package co.homecenter.etq.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.homecenter.etq.api.dto.request.EtqConsultaRequest;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.EtqDetailResponse;
import co.homecenter.etq.api.exception.LpnNotFoundException;
import co.homecenter.etq.application.service.EtqQueryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/etq")
public class EtqController {

    private final EtqQueryService etqQueryService;

    public EtqController(EtqQueryService etqQueryService) {
        this.etqQueryService = etqQueryService;
    }

    @PostMapping("/consulta")
    public ResponseEntity<ApiResponse<EtqDetailResponse>> consultar(
            @Valid @RequestBody EtqConsultaRequest request) {
        String lpn = request.getRequest().getLpn().trim();
        EtqDetailResponse detail = etqQueryService.consultarPorLpn(lpn)
                .orElseThrow(() -> new LpnNotFoundException(lpn));
        return ResponseEntity.ok(ApiResponse.success("ETQ_FOUND", "Consulta de ETQ exitosa", detail));
    }
}
