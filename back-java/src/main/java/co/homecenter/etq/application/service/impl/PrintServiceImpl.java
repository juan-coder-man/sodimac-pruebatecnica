package co.homecenter.etq.application.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.homecenter.etq.api.dto.request.PrintRequest;
import co.homecenter.etq.api.dto.response.ApiResponse;
import co.homecenter.etq.api.dto.response.PrintResponseData;
import co.homecenter.etq.application.service.PrintService;
import co.homecenter.etq.domain.enums.EventType;
import co.homecenter.etq.domain.enums.PrintResult;
import co.homecenter.etq.domain.model.Label;
import co.homecenter.etq.domain.model.Order;
import co.homecenter.etq.domain.model.PrintAudit;
import co.homecenter.etq.domain.repository.OrderRepository;
import co.homecenter.etq.domain.repository.PrintAuditRepository;
import co.homecenter.etq.domain.rule.PrintValidationService;
import co.homecenter.etq.domain.rule.ValidationOutcome;

@Service
public class PrintServiceImpl implements PrintService {

    private final OrderRepository orderRepository;
    private final PrintValidationService printValidationService;
    private final PrintAuditRepository printAuditRepository;

    public PrintServiceImpl(
            OrderRepository orderRepository,
            PrintValidationService printValidationService,
            PrintAuditRepository printAuditRepository) {
        this.orderRepository = orderRepository;
        this.printValidationService = printValidationService;
        this.printAuditRepository = printAuditRepository;
    }

    @Override
    public ApiResponse<PrintResponseData> imprimir(PrintRequest request) {
        String lpn = request.getLpn().trim();
        String zone = request.getZone().trim();
        String requestedBy = request.getRequestedBy().trim();
        String reprintReason = request.getReprintReason();

        String requestId = "REQ-PRINT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Instant printedAt = Instant.now();

        Optional<Order> orderOpt = orderRepository.findByLpn(lpn);
        if (orderOpt.isEmpty()) {
            PrintResponseData data = buildAndSaveRejected(
                    requestId,
                    null,
                    lpn,
                    zone,
                    requestedBy,
                    printedAt,
                    "LPN no encontrado en los datos mock",
                    reprintReason,
                    null);
            return ApiResponse.failure(
                    "LPN_NOT_FOUND",
                    "No se encontro ETQ para el LPN indicado",
                    data);
        }

        Order order = orderOpt.get();
        Optional<Label> labelOpt = findLabel(order, lpn);
        if (labelOpt.isEmpty()) {
            PrintResponseData data = buildAndSaveRejected(
                    requestId,
                    null,
                    lpn,
                    zone,
                    requestedBy,
                    printedAt,
                    "LPN sin etiqueta asociada",
                    reprintReason,
                    null);
            return ApiResponse.failure(
                    "LPN_NOT_FOUND",
                    "No se encontro ETQ para el LPN indicado",
                    data);
        }

        Label label = labelOpt.get();
        ValidationOutcome outcome = printValidationService.validate(order, zone, lpn);

        if (!outcome.isAllowed()) {
            PrintResponseData data = buildAndSaveRejected(
                    requestId,
                    label.getEtqId(),
                    lpn,
                    zone,
                    requestedBy,
                    printedAt,
                    outcome.getReason(),
                    reprintReason,
                    null);
            return ApiResponse.failure(outcome.getCode(), outcome.getReason(), data);
        }

        EventType eventType = outcome.isReprint() ? EventType.REIMPRESION : EventType.IMPRESION;
        PrintAudit audit = new PrintAudit();
        audit.setId(UUID.randomUUID().toString());
        audit.setRequestId(requestId);
        audit.setEtqId(label.getEtqId());
        audit.setLpnId(lpn);
        audit.setZone(zone);
        audit.setRequestedBy(requestedBy);
        audit.setPrintedAt(printedAt);
        audit.setResult(PrintResult.EXITOSO);
        audit.setEventType(eventType);
        audit.setReason(null);
        audit.setReprintReason(outcome.isReprint() ? reprintReason : null);
        audit.setZpl(label.getZpl());
        printAuditRepository.save(audit);

        PrintResponseData data = toResponseData(audit);
        String code = outcome.isReprint() ? "REPRINT_OK" : "PRINT_OK";
        String message = outcome.isReprint()
                ? "Reimpresion de ETQ exitosa"
                : "Impresion de ETQ exitosa";
        return ApiResponse.success(code, message, data);
    }

    private PrintResponseData buildAndSaveRejected(
            String requestId,
            String etqId,
            String lpn,
            String zone,
            String requestedBy,
            Instant printedAt,
            String reason,
            String reprintReason,
            String zpl) {
        PrintAudit audit = new PrintAudit();
        audit.setId(UUID.randomUUID().toString());
        audit.setRequestId(requestId);
        audit.setEtqId(etqId);
        audit.setLpnId(lpn);
        audit.setZone(zone);
        audit.setRequestedBy(requestedBy);
        audit.setPrintedAt(printedAt);
        audit.setResult(PrintResult.RECHAZADO);
        audit.setEventType(EventType.IMPRESION);
        audit.setReason(reason);
        audit.setReprintReason(reprintReason);
        audit.setZpl(zpl);
        printAuditRepository.save(audit);
        return toResponseData(audit);
    }

    private PrintResponseData toResponseData(PrintAudit audit) {
        PrintResponseData data = new PrintResponseData();
        data.setRequestId(audit.getRequestId());
        data.setEtqId(audit.getEtqId());
        data.setLpnId(audit.getLpnId());
        data.setZone(audit.getZone());
        data.setEventType(audit.getEventType() != null ? audit.getEventType().name() : null);
        data.setResult(audit.getResult() != null ? audit.getResult().name() : null);
        data.setReason(audit.getReason());
        data.setZpl(audit.getZpl());
        data.setPrintedAt(audit.getPrintedAt() != null ? audit.getPrintedAt().toString() : null);
        data.setReprintReason(audit.getReprintReason());
        return data;
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
