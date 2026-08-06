package co.homecenter.etq.domain.model;

public class InventoryItem {

    private String zone;
    private String productCode;
    private int availableQty;
    private boolean supplied;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public boolean isSupplied() {
        return supplied;
    }

    public void setSupplied(boolean supplied) {
        this.supplied = supplied;
    }
}
