package demo.infrastructure.adapters.out.http;

import demo.application.usecase.DocumentEvent;
import demo.domain.Document;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import demo.ports.out.MetadataServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetadataAdapter implements MetadataServicePort {

    private final RestTemplate rest = new RestTemplate();
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${services.metadata.base-url:http://metadata-service:8083}")
    private String metadataBaseUrl;

    @Override
    public void saveMetadata(DocumentEvent event) {
        log.info("Publishing metadata event for document '{}'", event.getDocumentId());
        kafkaTemplate.send("metadata-events", event.getDocumentId(), event);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean checkAccess(String documentId, String user) {
        log.info("Checking metadata access for document '{}' and user '{}'", documentId, user);
        Map<String, Object> response = rest.getForObject(
                metadataBaseUrl + "/metadata/" + documentId + "/access/" + user,
                Map.class);
        if (response == null) {
            return false;
        }

        Map<String, Object> data = (Map<String, Object>) response.get("data");
        return data != null && Boolean.TRUE.equals(data.get("allowed"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public java.util.Optional<Document> getMetadata(String documentId) {
        try {
            Map<String, Object> response = rest.getForObject(
                    metadataBaseUrl + "/metadata/" + documentId,
                    Map.class);
            if (response == null) {
                return java.util.Optional.empty();
            }

            return java.util.Optional.ofNullable(toDocument((Map<String, Object>) response.get("data")));
        } catch (HttpClientErrorException.NotFound ex) {
            return java.util.Optional.empty();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Document> listByOwner(String owner) {
        Map<String, Object> response = rest.getForObject(
                metadataBaseUrl + "/metadata/owner/" + owner,
                Map.class);
        if (response == null) {
            return List.of();
        }

        Object data = response.get("data");
        if (!(data instanceof List<?> rawList)) {
            return List.of();
        }

        return rawList.stream()
                .filter(Map.class::isInstance)
                .map(item -> toDocument((Map<String, Object>) item))
                .toList();
    }

    private Document toDocument(Map<String, Object> data) {
        if (data == null) {
            return null;
        }

        return Document.builder()
                .id(asString(data.get("documentId")))
                .title(asString(data.get("title")))
                .fileUrl(asString(data.get("fileUrl")))
                .owner(asString(data.get("owner")))
                .parentId(asString(data.get("parentId")))
                .expedienteId(asString(data.get("expedienteId")))
                .tipoDocumental(asString(data.get("tipoDocumental")))
                .estado(asString(data.get("estado")))
                .version(asInteger(data.get("version")))
                .build();
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private Integer asInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        return Integer.parseInt(value.toString());
    }

}
