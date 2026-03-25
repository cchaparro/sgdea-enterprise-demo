package demo.infrastructure.adapters.out.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import demo.ports.out.EventPublisherPort;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaPublisherAdapter implements EventPublisherPort {

    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void publishMetadata(Object event) {
        kafka.send("metadata-events", event);
    }

    @Override
    public void publishAudit(Object event) {
        kafka.send("audit-events", event);
    }


    
}
