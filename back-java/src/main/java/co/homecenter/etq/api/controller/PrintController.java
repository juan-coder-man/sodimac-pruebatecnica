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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/print")
@Tag(name = "Print", description = "Impresion y reimpresion de ETQ")
public class PrintController {

    private final PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @PostMapping
    @Operation(summary = "Procesar solicitud de impresion")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Exito o rechazo de negocio (success true/false)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Validacion de entrada")
    })
    public ResponseEntity<ApiResponse<PrintResponseData>> imprimir(
            @Valid @RequestBody PrintRequest request) {
        return ResponseEntity.ok(printService.imprimir(request));
    }
}
