package co.homecenter.etq.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PrintFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void consultaLpnSeedReturns200() throws Exception {
        mockMvc.perform(post("/api/v1/etq/consulta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"request\":{\"lpn\":\"LPN-000987654\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("ETQ_FOUND"));
    }

    @Test
    @Order(2)
    void printSeedLpnReturnsPrintOk() throws Exception {
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
    @Order(3)
    void printAgainReturnsReprintOk() throws Exception {
        mockMvc.perform(post("/api/v1/print")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "lpn": "LPN-000987654",
                                  "zone": "ZONA-PICKING-A",
                                  "requestedBy": "usuario.operacion",
                                  "reprintReason": "Etiqueta danada"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("REPRINT_OK"));
    }

    @Test
    @Order(4)
    void printAnuladaReturnsDocumentInvalidStatus() throws Exception {
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

    @Test
    @Order(5)
    void historyReturnsNonEmptyList() throws Exception {
        mockMvc.perform(get("/api/v1/print/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("HISTORY_OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(org.hamcrest.Matchers.greaterThan(0)));
    }
}
