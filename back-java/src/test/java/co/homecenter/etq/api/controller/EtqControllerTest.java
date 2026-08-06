package co.homecenter.etq.api.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import co.homecenter.etq.api.dto.response.EtqDetailResponse;
import co.homecenter.etq.api.exception.GlobalExceptionHandler;
import co.homecenter.etq.application.service.EtqQueryService;

@WebMvcTest(controllers = EtqController.class)
@Import(GlobalExceptionHandler.class)
class EtqControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EtqQueryService etqQueryService;

    @Test
    void consultaOkReturns200AndEtqFound() throws Exception {
        EtqDetailResponse detail = new EtqDetailResponse();
        detail.setIdEtiqueta("ETQ-10001");
        detail.setLpnId("LPN-000987654");

        when(etqQueryService.consultarPorLpn(eq("LPN-000987654"))).thenReturn(Optional.of(detail));

        mockMvc.perform(post("/api/v1/etq/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"request\":{\"lpn\":\"LPN-000987654\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("ETQ_FOUND"))
                .andExpect(jsonPath("$.data.lpnId").value("LPN-000987654"));
    }

    @Test
    void consultaEmptyLpnReturns400ValidationError() throws Exception {
        mockMvc.perform(post("/api/v1/etq/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"request\":{\"lpn\":\"\"}}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void consultaUnknownLpnReturns404() throws Exception {
        when(etqQueryService.consultarPorLpn(eq("LPN-NO-EXISTE"))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/etq/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"request\":{\"lpn\":\"LPN-NO-EXISTE\"}}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("LPN_NOT_FOUND"));
    }
}
