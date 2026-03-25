package demo.application.usecase;

import demo.domain.model.Metadata;
import demo.domain.model.MetadataEvent;
import demo.domain.ports.in.ConsumeMetadataEventUseCase;
import demo.domain.ports.out.MetadataEventRepositoryPort;
import demo.domain.ports.out.MetadataRepositoryPort;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumeMetadataEventUseCaseImpl implements ConsumeMetadataEventUseCase {

    private final MetadataEventRepositoryPort metadataEventRepositoryPort;
    private final MetadataRepositoryPort metadataRepositoryPort;

    @Override
    public void consume(Map<String, Object> event) {
        MetadataEvent metadataEvent = new MetadataEvent();
        metadataEvent.setEventType(asString(event.get("eventType")));
        metadataEvent.setDocumentId(asString(event.get("documentId")));
        metadataEvent.setTitle(asString(event.get("title")));
        metadataEvent.setFileUrl(asString(event.get("fileUrl")));
        metadataEvent.setParentId(asString(event.get("parentId")));
        metadataEvent.setOwner(asString(event.get("owner")));
        metadataEvent.setTraceId(asString(event.get("traceId")));
        metadataEvent.setOccurredAt(parseInstant(event.get("timestamp")));
        metadataEvent.setReceivedAt(Instant.now());

        metadataEventRepositoryPort.save(metadataEvent);
        upsertMetadata(metadataEvent);
        log.info("Metadata event persisted for document '{}'", metadataEvent.getDocumentId());
    }

    private void upsertMetadata(MetadataEvent metadataEvent) {
        if (metadataEvent.getDocumentId() == null || metadataEvent.getOwner() == null) {
            return;
        }

        Metadata metadata = metadataRepositoryPort.findById(metadataEvent.getDocumentId())
                .orElseGet(Metadata::new);
        metadata.setDocumentId(metadataEvent.getDocumentId());
        metadata.setOwner(metadataEvent.getOwner());
        metadataRepositoryPort.save(metadata);
    }

    private String asString(Object value) {
        return value == null ? null : value.toString();
    }

    private Instant parseInstant(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return Instant.parse(value.toString());
        } catch (DateTimeParseException ex) {
            log.warn("Could not parse metadata event timestamp '{}'", value);
            return null;
        }
    }
}
