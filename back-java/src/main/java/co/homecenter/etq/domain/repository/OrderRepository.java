package co.homecenter.etq.domain.repository;

import java.util.Optional;

import co.homecenter.etq.domain.model.Order;

public interface OrderRepository {

    Optional<Order> findByLpn(String lpn);

    Optional<Order> findByEtqId(String etqId);
}
