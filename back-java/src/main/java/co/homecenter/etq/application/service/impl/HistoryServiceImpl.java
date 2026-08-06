package co.homecenter.etq.application.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.homecenter.etq.api.dto.response.HistoryItemResponse;
import co.homecenter.etq.application.service.HistoryService;
import co.homecenter.etq.domain.enums.PrintResult;
import co.homecenter.etq.domain.model.PrintAudit;
import co.homecenter.etq.domain.repository.PrintAuditRepository;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final PrintAuditRepository printAuditRepository;

    public HistoryServiceImpl(PrintAuditRepository printAuditRepository) {
        this.printAuditRepository = printAuditRepository;
    }

    @Override
    public List<HistoryItemResponse> listar(String lpn, String zone, PrintResult result) {
        return printAuditRepository.findAll().stream()
                .filter(audit -> matchesLpn(audit, lpn))
                .filter(audit -> matchesZone(audit, zone))
                .filter(audit -> matchesResult(audit, result))
                .sorted(Comparator.comparing(
                        PrintAudit::getPrintedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toResponse)
                .collect(Collectors.toList());
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
