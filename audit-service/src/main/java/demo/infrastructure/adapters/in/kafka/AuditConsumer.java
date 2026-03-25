package demo.infrastructure.adapters.in.kafka;

import demo.domain.ports.in.ConsumeAuditEventUseCase;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditConsumer {

    private final ConsumeAuditEventUseCase consumeAuditEventUseCase;

    @KafkaListener(topics = "audit-events", groupId = "audit-service")
    public void consume(Map<String, Object> event) {
        log.info("Received audit event from Kafka");
        consumeAuditEventUseCase.consume(event);
    }
}
