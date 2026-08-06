package co.homecenter.etq.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

import co.homecenter.etq.domain.enums.PrintResult;
import co.homecenter.etq.domain.model.PrintAudit;
import co.homecenter.etq.domain.repository.PrintAuditRepository;

@Repository
public class InMemoryPrintAuditRepository implements PrintAuditRepository {

    private final List<PrintAudit> audits = new CopyOnWriteArrayList<>();

    @Override
    public void save(PrintAudit audit) {
        audits.add(audit);
    }

    @Override
    public boolean existsSuccessfulByLpn(String lpn) {
        if (lpn == null || lpn.isBlank()) {
            return false;
        }
        return audits.stream()
                .anyMatch(audit -> lpn.equals(audit.getLpnId())
                        && audit.getResult() == PrintResult.EXITOSO);
    }

    @Override
    public List<PrintAudit> findAll() {
        return new ArrayList<>(audits);
    }
}
