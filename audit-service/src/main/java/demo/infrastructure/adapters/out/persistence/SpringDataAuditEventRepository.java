package demo.infrastructure.adapters.out.persistence;

import demo.domain.model.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAuditEventRepository extends JpaRepository<AuditEvent, Long> {
}
