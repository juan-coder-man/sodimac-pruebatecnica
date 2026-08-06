package co.homecenter.etq.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import co.homecenter.etq.api.dto.response.EtqDetailResponse;
import co.homecenter.etq.api.dto.response.EtqProductSummary;
import co.homecenter.etq.domain.model.Label;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.model.Product;

@Component
public class EtqMapper {

    public EtqDetailResponse toDetailResponse(Order order, Label label) {
        EtqDetailResponse response = new EtqDetailResponse();
        response.setIdEtiqueta(label.getEtqId());
        response.setLpnId(label.getLpnId());
        response.setZpl(label.getZpl());
        response.setZone(order.getZone());
        response.setTcOrderId(order.getRequestId());

        if (order.getDocument() != null) {
            response.setPurchaseOrder(order.getDocument().getDocumentNumber());
            if (order.getDocument().getStatus() != null) {
                response.setDocumentStatus(order.getDocument().getStatus().name());
            }
        }

        List<Product> products = order.getProducts();
        if (products != null && !products.isEmpty()) {
            response.setSku(products.get(0).getProductCode());
            response.setUnidades(products.stream().mapToInt(Product::getRequestedQty).sum());
            response.setProducts(products.stream()
                    .map(product -> new EtqProductSummary(
                            product.getProductCode(),
                            product.getProductDescription(),
                            product.getRequestedQty(),
                            product.getUom()))
                    .collect(Collectors.toList()));
        } else {
            response.setSku(null);
            response.setUnidades(0);
        }

        return response;
    }
}
