package co.homecenter.etq.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "etq.mocks")
public class MockDataProperties {

    private String ordersPath = "classpath:mocks/orders.json";
    private String inventoryPath = "classpath:mocks/inventory.json";

    public String getOrdersPath() {
        return ordersPath;
    }

    public void setOrdersPath(String ordersPath) {
        this.ordersPath = ordersPath;
    }

    public String getInventoryPath() {
        return inventoryPath;
    }

    public void setInventoryPath(String inventoryPath) {
        this.inventoryPath = inventoryPath;
    }
}
