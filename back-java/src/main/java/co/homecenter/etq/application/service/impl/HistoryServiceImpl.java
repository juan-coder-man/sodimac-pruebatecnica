package co.homecenter.etq.application.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.homecenter.etq.api.dto.response.HistoryItemResponse;
import co.homecenter.etq.application.service.HistoryService;
import co.homecenter.etq.domain.enums.PrintResult;
import co.homecenter.etq.domain.model.PrintAudit;
import co.homecenter.etq.domain.repository.PrintAuditRepository;

@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);

    private final PrintAuditRepository printAuditRepository;

    public HistoryServiceImpl(PrintAuditRepository printAuditRepository) {
        this.printAuditRepository = printAuditRepository;
    }

    @Override
    public List<HistoryItemResponse> listar(String lpn, String zone, PrintResult result) {
        log.info("Consulta historial filtros lpn={} zone={} result={}", lpn, zone, result);
        List<HistoryItemResponse> items = printAuditRepository.findAll().stream()
                .filter(audit -> matchesLpn(audit, lpn))
                .filter(audit -> matchesZone(audit, zone))
                .filter(audit -> matchesResult(audit, result))
                .sorted(Comparator.comparing(
                        PrintAudit::getPrintedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toResponse)
                .collect(Collectors.toList());
        log.info("Historial retornado count={}", items.size());
        return items;
    }

    private boolean matchesLpn(PrintAudit audit, String lpn) {
        if (lpn == null || lpn.isBlank()) {
            return true;
        }
        return lpn.equals(audit.getLpnId());
    }

    private boolean matchesZone(PrintAudit audit, String zone) {
        if (zone == null || zone.isBlank()) {
            return true;
        }
        return zone.equals(audit.getZone());
    }

    private boolean matchesResult(PrintAudit audit, PrintResult result) {
        if (result == null) {
            return true;
        }
        return result == audit.getResult();
    }

    private HistoryItemResponse toResponse(PrintAudit audit) {
        HistoryItemResponse item = new HistoryItemResponse();
        item.setEtqId(audit.getEtqId());
        item.setLpnId(audit.getLpnId());
        item.setZone(audit.getZone());
        item.setRequestedBy(audit.getRequestedBy());
        item.setPrintedAt(audit.getPrintedAt() != null ? audit.getPrintedAt().toString() : null);
        item.setResult(audit.getResult() != null ? audit.getResult().name() : null);
        item.setEventType(audit.getEventType() != null ? audit.getEventType().name() : null);
        item.setReason(audit.getReason());
        item.setReprintReason(audit.getReprintReason());
        return item;
    }
}
