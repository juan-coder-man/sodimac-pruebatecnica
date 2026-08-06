package co.homecenter.etq.api.dto.response;

public class EtqProductSummary {

    private String productCode;
    private String productDescription;
    private int requestedQty;
    private String uom;

    public EtqProductSummary() {
    }

    public EtqProductSummary(String productCode, String productDescription, int requestedQty, String uom) {
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.requestedQty = requestedQty;
        this.uom = uom;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getRequestedQty() {
        return requestedQty;
    }

    public void setRequestedQty(int requestedQty) {
        this.requestedQty = requestedQty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
