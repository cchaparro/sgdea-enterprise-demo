
package com.template.infrastructure.adapters.out.kafka;

import com.template.domain.ports.out.KafkaPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaAdapter implements KafkaPort {

    private final KafkaTemplate<String, Object> kafka;

    @Override
    public void send(String topic, Object message) {
        kafka.send(topic, message);
    }
}
