package co.homecenter.etq.domain.repository;

import java.util.List;

import co.homecenter.etq.domain.model.PrintAudit;

public interface PrintAuditRepository {

    void save(PrintAudit audit);

    boolean existsSuccessfulByLpn(String lpn);

    List<PrintAudit> findAll();
}
