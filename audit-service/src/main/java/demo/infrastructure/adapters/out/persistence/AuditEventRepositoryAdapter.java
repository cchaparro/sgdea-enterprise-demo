package demo.infrastructure.adapters.out.persistence;

import demo.domain.model.AuditEvent;
import demo.domain.ports.out.AuditEventRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditEventRepositoryAdapter implements AuditEventRepositoryPort {

    private final SpringDataAuditEventRepository repository;

    @Override
    public AuditEvent save(AuditEvent event) {
        return repository.save(event);
    }
}
