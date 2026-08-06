package co.homecenter.etq.api.dto.response;

import java.util.ArrayList;
import java.util.List;

public class EtqDetailResponse {

    private String idEtiqueta;
    private String purchaseOrder;
    private String tcOrderId;
    private String sku;
    private int unidades;
    private String zpl;
    private String lpnId;
    private String zone;
    private String documentStatus;
    private List<EtqProductSummary> products = new ArrayList<>();

    public String getIdEtiqueta() {
        return idEtiqueta;
    }

    public void setIdEtiqueta(String idEtiqueta) {
        this.idEtiqueta = idEtiqueta;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getTcOrderId() {
        return tcOrderId;
    }

    public void setTcOrderId(String tcOrderId) {
        this.tcOrderId = tcOrderId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public String getZpl() {
        return zpl;
    }

    public void setZpl(String zpl) {
        this.zpl = zpl;
    }

    public String getLpnId() {
        return lpnId;
    }

    public void setLpnId(String lpnId) {
        this.lpnId = lpnId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(String documentStatus) {
        this.documentStatus = documentStatus;
    }

    public List<EtqProductSummary> getProducts() {
        return products;
    }

    public void setProducts(List<EtqProductSummary> products) {
        this.products = products != null ? products : new ArrayList<>();
    }
}
