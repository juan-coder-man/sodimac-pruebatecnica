package co.homecenter.etq.domain.rule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.homecenter.etq.domain.enums.DocumentStatus;
import co.homecenter.etq.domain.model.Document;
import co.homecenter.etq.domain.model.InventoryItem;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.model.Product;
import co.homecenter.etq.domain.repository.InventoryRepository;
import co.homecenter.etq.domain.repository.PrintAuditRepository;

@ExtendWith(MockitoExtension.class)
class PrintValidationServiceTest {

    private static final String ZONE = "ZONA-PICKING-A";
    private static final String LPN = "LPN-000987654";
    private static final String PRODUCT_CODE = "PROD-001";

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private PrintAuditRepository printAuditRepository;

    private PrintValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new PrintValidationService(inventoryRepository, printAuditRepository);
    }

    @Test
    void rejectsWhenDocumentIsAnulada() {
        Order order = buildOrder(DocumentStatus.ANULADA, PRODUCT_CODE, 2);

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertFalse(outcome.isAllowed());
        assertEquals("DOCUMENT_INVALID_STATUS", outcome.getCode());
        assertFalse(outcome.isReprint());
    }

    @Test
    void rejectsWhenDocumentIsDevuelta() {
        Order order = buildOrder(DocumentStatus.DEVUELTA, PRODUCT_CODE, 2);

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertFalse(outcome.isAllowed());
        assertEquals("DOCUMENT_INVALID_STATUS", outcome.getCode());
    }

    @Test
    void rejectsWhenProductHasNoInventoryItem() {
        Order order = buildOrder(DocumentStatus.LIBERADA, PRODUCT_CODE, 2);
        when(inventoryRepository.findByZoneAndProduct(ZONE, PRODUCT_CODE)).thenReturn(Optional.empty());

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertFalse(outcome.isAllowed());
        assertEquals("PRODUCT_NOT_SUPPLIED", outcome.getCode());
    }

    @Test
    void rejectsWhenProductIsNotSupplied() {
        Order order = buildOrder(DocumentStatus.LIBERADA, PRODUCT_CODE, 2);
        when(inventoryRepository.findByZoneAndProduct(ZONE, PRODUCT_CODE))
                .thenReturn(Optional.of(buildInventory(PRODUCT_CODE, 50, false)));

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertFalse(outcome.isAllowed());
        assertEquals("PRODUCT_NOT_SUPPLIED", outcome.getCode());
    }

    @Test
    void rejectsWhenAvailableQtyIsInsufficient() {
        Order order = buildOrder(DocumentStatus.LIBERADA, PRODUCT_CODE, 10);
        when(inventoryRepository.findByZoneAndProduct(ZONE, PRODUCT_CODE))
                .thenReturn(Optional.of(buildInventory(PRODUCT_CODE, 3, true)));

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertFalse(outcome.isAllowed());
        assertEquals("INSUFFICIENT_INVENTORY", outcome.getCode());
    }

    @Test
    void allowsWhenEverythingIsValidAndNoPriorPrint() {
        Order order = buildOrder(DocumentStatus.LIBERADA, PRODUCT_CODE, 2);
        when(inventoryRepository.findByZoneAndProduct(eq(ZONE), anyString()))
                .thenReturn(Optional.of(buildInventory(PRODUCT_CODE, 50, true)));
        when(printAuditRepository.existsSuccessfulByLpn(LPN)).thenReturn(false);

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertTrue(outcome.isAllowed());
        assertFalse(outcome.isReprint());
    }

    @Test
    void allowsAsReprintWhenSuccessfulPrintExists() {
        Order order = buildOrder(DocumentStatus.LIBERADA, PRODUCT_CODE, 2);
        when(inventoryRepository.findByZoneAndProduct(eq(ZONE), anyString()))
                .thenReturn(Optional.of(buildInventory(PRODUCT_CODE, 50, true)));
        when(printAuditRepository.existsSuccessfulByLpn(LPN)).thenReturn(true);

        ValidationOutcome outcome = validationService.validate(order, ZONE, LPN);

        assertTrue(outcome.isAllowed());
        assertTrue(outcome.isReprint());
    }

    private Order buildOrder(DocumentStatus status, String productCode, int requestedQty) {
        Document document = new Document();
        document.setDocumentType("OC");
        document.setDocumentNumber("PO-100");
        document.setStatus(status);

        Product product = new Product();
        product.setProductCode(productCode);
        product.setProductDescription("Producto test");
        product.setRequestedQty(requestedQty);
        product.setUom("UN");

        Order order = new Order();
        order.setRequestId("REQ-1");
        order.setZone(ZONE);
        order.setDocument(document);
        order.setProducts(List.of(product));
        return order;
    }

    private InventoryItem buildInventory(String productCode, int availableQty, boolean supplied) {
        InventoryItem item = new InventoryItem();
        item.setZone(ZONE);
        item.setProductCode(productCode);
        item.setAvailableQty(availableQty);
        item.setSupplied(supplied);
        return item;
    }
}
