package co.homecenter.etq.domain.repository;

import java.util.List;
import java.util.Optional;

import co.homecenter.etq.domain.model.InventoryItem;

public interface InventoryRepository {

    Optional<InventoryItem> findByZoneAndProduct(String zone, String productCode);

    List<InventoryItem> findByZone(String zone);
}
