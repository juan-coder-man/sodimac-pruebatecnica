package co.homecenter.etq.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import co.homecenter.etq.api.dto.response.EtqDetailResponse;
import co.homecenter.etq.domain.enums.DocumentStatus;
import co.homecenter.etq.domain.model.Document;
import co.homecenter.etq.domain.model.Label;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.model.Product;

class EtqMapperTest {

    private EtqMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EtqMapper();
    }

    @Test
    void mapsCoreFieldsSkuUnidadesAndProducts() {
        Order order = new Order();
        order.setRequestId("REQ-100");
        order.setZone("ZONA-PICKING-A");

        Document document = new Document();
        document.setDocumentNumber("PO-998877");
        document.setStatus(DocumentStatus.LIBERADA);
        order.setDocument(document);

        Product p1 = new Product();
        p1.setProductCode("PROD-001");
        p1.setProductDescription("Tornillo");
        p1.setRequestedQty(2);
        p1.setUom("UN");

        Product p2 = new Product();
        p2.setProductCode("PROD-002");
        p2.setProductDescription("Tuerca");
        p2.setRequestedQty(1);
        p2.setUom("UN");

        order.setProducts(List.of(p1, p2));

        Label label = new Label();
        label.setEtqId("ETQ-10001");
        label.setLpnId("LPN-000987654");
        label.setZpl("^XA^FO50,50^FDTEST^FS^XZ");

        EtqDetailResponse response = mapper.toDetailResponse(order, label);

        assertEquals("ETQ-10001", response.getIdEtiqueta());
        assertEquals("LPN-000987654", response.getLpnId());
        assertEquals("^XA^FO50,50^FDTEST^FS^XZ", response.getZpl());
        assertEquals("ZONA-PICKING-A", response.getZone());
        assertEquals("PO-998877", response.getPurchaseOrder());
        assertEquals("LIBERADA", response.getDocumentStatus());
        assertEquals("PROD-001", response.getSku());
        assertEquals(3, response.getUnidades());
        assertNotNull(response.getProducts());
        assertFalse(response.getProducts().isEmpty());
        assertEquals(2, response.getProducts().size());
    }
}
