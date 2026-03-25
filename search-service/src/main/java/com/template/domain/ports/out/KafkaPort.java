
package com.template.domain.ports.out;

public interface KafkaPort {
    void send(String topic, Object message);
}
