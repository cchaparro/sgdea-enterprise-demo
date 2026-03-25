package demo.domain.ports.out;

import demo.domain.model.AuditEvent;

public interface AuditEventRepositoryPort {

    AuditEvent save(AuditEvent event);
}
