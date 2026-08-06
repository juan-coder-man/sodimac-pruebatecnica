package co.homecenter.etq.infrastructure.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.homecenter.etq.domain.model.InventoryItem;
import co.homecenter.etq.domain.repository.InventoryRepository;
import co.homecenter.etq.infrastructure.config.MockDataProperties;
import jakarta.annotation.PostConstruct;

@Repository
public class InventoryMockRepository implements InventoryRepository {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MockDataProperties mockDataProperties;

    private final Map<String, InventoryItem> byZoneAndProduct = new HashMap<>();
    private final List<InventoryItem> allItems = new ArrayList<>();

    public InventoryMockRepository(
            ResourceLoader resourceLoader,
            MockDataProperties mockDataProperties) {
        this.resourceLoader = resourceLoader;
        this.mockDataProperties = mockDataProperties;
    }

    @PostConstruct
    void load() throws IOException {
        Resource resource = resourceLoader.getResource(mockDataProperties.getInventoryPath());
        try (InputStream inputStream = resource.getInputStream()) {
            List<InventoryItem> items = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            allItems.clear();
            allItems.addAll(items);
            byZoneAndProduct.clear();
            for (InventoryItem item : items) {
                byZoneAndProduct.put(key(item.getZone(), item.getProductCode()), item);
            }
        }
    }

    @Override
    public Optional<InventoryItem> findByZoneAndProduct(String zone, String productCode) {
        if (zone == null || productCode == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(byZoneAndProduct.get(key(zone, productCode)));
    }

    @Override
    public List<InventoryItem> findByZone(String zone) {
        if (zone == null) {
            return List.of();
        }
        return allItems.stream()
                .filter(item -> zone.equals(item.getZone()))
                .collect(Collectors.toList());
    }

    private static String key(String zone, String productCode) {
        return zone + "|" + productCode;
    }
}
