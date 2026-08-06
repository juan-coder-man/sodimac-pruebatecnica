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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/etq")
@Tag(name = "ETQ", description = "Consulta de etiquetas pre-generadas")
public class EtqController {

    private final EtqQueryService etqQueryService;

    public EtqController(EtqQueryService etqQueryService) {
        this.etqQueryService = etqQueryService;
    }

    @PostMapping("/consulta")
    @Operation(summary = "Consultar ETQ por LPN")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "ETQ encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Validacion de entrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "LPN no encontrado")
    })
    public ResponseEntity<ApiResponse<EtqDetailResponse>> consultar(
            @Valid @RequestBody EtqConsultaRequest request) {
        String lpn = request.getRequest().getLpn().trim();
        EtqDetailResponse detail = etqQueryService.consultarPorLpn(lpn)
                .orElseThrow(() -> new LpnNotFoundException(lpn));
        return ResponseEntity.ok(ApiResponse.success("ETQ_FOUND", "Consulta de ETQ exitosa", detail));
    }
}
