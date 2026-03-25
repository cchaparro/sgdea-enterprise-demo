package demo.infrastructure.adapters.out.http;

import demo.application.usecase.DocumentEvent;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
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

}
