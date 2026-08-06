package co.homecenter.etq.application.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.homecenter.etq.api.dto.response.EtqDetailResponse;
import co.homecenter.etq.application.mapper.EtqMapper;
import co.homecenter.etq.application.service.EtqQueryService;
import co.homecenter.etq.domain.model.Label;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.repository.OrderRepository;

@Service
public class EtqQueryServiceImpl implements EtqQueryService {

    private static final Logger log = LoggerFactory.getLogger(EtqQueryServiceImpl.class);

    private final OrderRepository orderRepository;
    private final EtqMapper etqMapper;

    public EtqQueryServiceImpl(OrderRepository orderRepository, EtqMapper etqMapper) {
        this.orderRepository = orderRepository;
        this.etqMapper = etqMapper;
    }

    @Override
    public Optional<EtqDetailResponse> consultarPorLpn(String lpn) {
        log.info("Consulta ETQ por lpn={}", lpn);
        Optional<EtqDetailResponse> detail = orderRepository.findByLpn(lpn)
                .flatMap(order -> findLabel(order, lpn)
                        .map(label -> etqMapper.toDetailResponse(order, label)));
        if (detail.isEmpty()) {
            log.warn("ETQ no encontrada para lpn={}", lpn);
        } else {
            log.info("ETQ encontrada para lpn={}", lpn);
        }
        return detail;
    }

    private Optional<Label> findLabel(Order order, String lpn) {
        if (order.getLabels() == null) {
            return Optional.empty();
        }
        return order.getLabels().stream()
                .filter(label -> lpn.equals(label.getLpnId()))
                .findFirst();
    }
}
