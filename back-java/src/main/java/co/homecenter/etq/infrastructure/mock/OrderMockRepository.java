package co.homecenter.etq.infrastructure.mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.homecenter.etq.domain.model.Label;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.repository.OrderRepository;
import co.homecenter.etq.infrastructure.config.MockDataProperties;
import jakarta.annotation.PostConstruct;

@Repository
public class OrderMockRepository implements OrderRepository {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MockDataProperties mockDataProperties;

    private final Map<String, Order> byLpn = new HashMap<>();
    private final Map<String, Order> byEtqId = new HashMap<>();

    public OrderMockRepository(
            ResourceLoader resourceLoader,
            MockDataProperties mockDataProperties) {
        this.resourceLoader = resourceLoader;
        this.mockDataProperties = mockDataProperties;
    }

    @PostConstruct
    void load() throws IOException {
        Resource resource = resourceLoader.getResource(mockDataProperties.getOrdersPath());
        try (InputStream inputStream = resource.getInputStream()) {
            List<Order> orders = objectMapper.readValue(inputStream, new TypeReference<>() {
            });
            for (Order order : orders) {
                if (order.getLabels() == null) {
                    continue;
                }
                for (Label label : order.getLabels()) {
                    if (label.getLpnId() != null) {
                        byLpn.put(label.getLpnId(), order);
                    }
                    if (label.getEtqId() != null) {
                        byEtqId.put(label.getEtqId(), order);
                    }
                }
            }
        }
    }

    @Override
    public Optional<Order> findByLpn(String lpn) {
        if (lpn == null || lpn.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(byLpn.get(lpn));
    }

    @Override
    public Optional<Order> findByEtqId(String etqId) {
        if (etqId == null || etqId.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(byEtqId.get(etqId));
    }
}
