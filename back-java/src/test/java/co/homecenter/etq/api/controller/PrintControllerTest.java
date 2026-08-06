package co.homecenter.etq.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import co.homecenter.etq.api.dto.request.PrintRequest;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.PrintResponseData;
import co.homecenter.etq.api.exception.GlobalExceptionHandler;
import co.homecenter.etq.application.service.PrintService;

@WebMvcTest(controllers = PrintController.class)
@Import(GlobalExceptionHandler.class)
class PrintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrintService printService;

    @Test
    void incompleteBodyReturns400() throws Exception {
        mockMvc.perform(post("/api/v1/print")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lpn\":\"LPN-000987654\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void successFromServiceReturns200() throws Exception {
        PrintResponseData data = new PrintResponseData();
        data.setLpnId("LPN-000987654");
        when(printService.imprimir(any(PrintRequest.class)))
                .thenReturn(ApiResponse.success("PRINT_OK", "Impresion de ETQ exitosa", data));

        mockMvc.perform(post("/api/v1/print")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lpn": "LPN-000987654",
                                  "zone": "ZONA-PICKING-A",
                                  "requestedBy": "usuario.operacion"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("PRINT_OK"));
    }

    @Test
    void businessRejectionReturns200WithSuccessFalse() throws Exception {
        when(printService.imprimir(any(PrintRequest.class)))
                .thenReturn(ApiResponse.failure(
                        "DOCUMENT_INVALID_STATUS",
                        "El documento origen esta en estado ANULADA y no permite impresion"));

        mockMvc.perform(post("/api/v1/print")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lpn": "LPN-ANULADA-001",
                                  "zone": "ZONA-PICKING-A",
                                  "requestedBy": "usuario.operacion"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("DOCUMENT_INVALID_STATUS"));
    }
}
