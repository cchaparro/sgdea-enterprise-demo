package demo.infrastructure.adapters.in.kafka;

import demo.domain.ports.in.ConsumeMetadataEventUseCase;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetadataEventConsumer {

    private final ConsumeMetadataEventUseCase consumeMetadataEventUseCase;

    @KafkaListener(topics = "metadata-events")
    public void consume(Map<String, Object> event) {
        log.info("Received metadata event from Kafka");
        consumeMetadataEventUseCase.consume(event);
    }
}
