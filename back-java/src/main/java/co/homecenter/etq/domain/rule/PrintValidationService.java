package co.homecenter.etq.domain.rule;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import co.homecenter.etq.domain.enums.DocumentStatus;
import co.homecenter.etq.domain.model.InventoryItem;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.model.Product;
import co.homecenter.etq.domain.repository.InventoryRepository;
import co.homecenter.etq.domain.repository.PrintAuditRepository;

@Component
public class PrintValidationService {

    private static final Logger log = LoggerFactory.getLogger(PrintValidationService.class);

    private final InventoryRepository inventoryRepository;
    private final PrintAuditRepository printAuditRepository;

    public PrintValidationService(
            InventoryRepository inventoryRepository,
            PrintAuditRepository printAuditRepository) {
        this.inventoryRepository = inventoryRepository;
        this.printAuditRepository = printAuditRepository;
    }

    public ValidationOutcome validate(Order order, String zone, String lpn) {
        DocumentStatus status = order.getDocument() != null ? order.getDocument().getStatus() : null;
        if (status == DocumentStatus.ANULADA || status == DocumentStatus.DEVUELTA) {
            log.info("Validacion rechazo code=DOCUMENT_INVALID_STATUS status={}", status);
            return ValidationOutcome.rejected(
                    "DOCUMENT_INVALID_STATUS",
                    "El documento origen esta en estado " + status.name() + " y no permite impresion");
        }

        if (order.getProducts() != null) {
            for (Product product : order.getProducts()) {
                Optional<InventoryItem> inventory =
                        inventoryRepository.findByZoneAndProduct(zone, product.getProductCode());

                if (inventory.isEmpty()) {
                    log.info(
                            "Validacion rechazo code=PRODUCT_NOT_SUPPLIED product={} zone={}",
                            product.getProductCode(),
                            zone);
                    return ValidationOutcome.rejected(
                            "PRODUCT_NOT_SUPPLIED",
                            "El producto " + product.getProductCode()
                                    + " no esta abastecido en la zona " + zone);
                }

                InventoryItem item = inventory.get();
                if (!item.isSupplied()) {
                    log.info(
                            "Validacion rechazo code=PRODUCT_NOT_SUPPLIED product={} zone={}",
                            product.getProductCode(),
                            zone);
                    return ValidationOutcome.rejected(
                            "PRODUCT_NOT_SUPPLIED",
                            "El producto " + product.getProductCode()
                                    + " no esta abastecido en la zona " + zone);
                }

                if (item.getAvailableQty() < product.getRequestedQty()) {
                    log.info(
                            "Validacion rechazo code=INSUFFICIENT_INVENTORY product={} zone={}",
                            product.getProductCode(),
                            zone);
                    return ValidationOutcome.rejected(
                            "INSUFFICIENT_INVENTORY",
                            "Inventario insuficiente para el producto " + product.getProductCode()
                                    + " en la zona " + zone
                                    + " (disponible=" + item.getAvailableQty()
                                    + ", solicitado=" + product.getRequestedQty() + ")");
                }
            }
        }

        boolean reprint = printAuditRepository.existsSuccessfulByLpn(lpn);
        log.info("Validacion OK lpn={} reprint={}", lpn, reprint);
        return ValidationOutcome.ok(reprint);
    }
}
