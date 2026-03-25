package com.template.infrastructure.adapters.in.kafka;

import com.template.domain.model.DocumentSearchView;
import com.template.domain.ports.out.SearchRepositoryPort;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetadataEventConsumer {

    private final SearchRepositoryPort repository;

    @KafkaListener(topics = "metadata-events", groupId = "search-service")
    public void consume(Map<String, Object> event) {
        String eventType = asString(event.get("eventType"));
        if (eventType == null || !eventType.startsWith("document.")) {
            return;
        }

        DocumentSearchView document = DocumentSearchView.builder()
                .id(asString(event.get("documentId")))
                .title(asString(event.get("title")))
                .owner(asString(event.get("owner")))
                .fileUrl(asString(event.get("fileUrl")))
                .parentId(asString(event.get("parentId")))
                .expedienteId(asString(event.get("expedienteId")))
                .tipoDocumental(asString(event.get("tipoDocumental")))
                .estado(defaultValue(asString(event.get("estado")), "ACTIVO"))
                .version(asInteger(event.get("version")))
                .traceId(asString(event.get("traceId")))
                .build();

        repository.index(document);
        log.info("Indexed document '{}' into search", document.getId());
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private Integer asInteger(Object value) {
        if (value == null) {
            return 1;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            return 1;
        }
    }

    private String defaultValue(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }
}
