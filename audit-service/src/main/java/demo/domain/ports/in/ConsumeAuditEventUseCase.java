package demo.domain.ports.in;

import java.util.Map;

public interface ConsumeAuditEventUseCase {

    void consume(Map<String, Object> event);
}
