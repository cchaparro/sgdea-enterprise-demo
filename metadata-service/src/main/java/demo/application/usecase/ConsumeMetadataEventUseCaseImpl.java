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
        metadataEvent.setExpedienteId(asString(event.get("expedienteId")));
        metadataEvent.setTipoDocumental(asString(event.get("tipoDocumental")));
        metadataEvent.setEstado(asString(event.get("estado")));
        metadataEvent.setVersion(asInteger(event.get("version")));
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
        metadata.setTitle(metadataEvent.getTitle());
        metadata.setFileUrl(metadataEvent.getFileUrl());
        metadata.setParentId(metadataEvent.getParentId());
        metadata.setExpedienteId(metadataEvent.getExpedienteId());
        metadata.setTipoDocumental(metadataEvent.getTipoDocumental());
        metadata.setEstado(metadataEvent.getEstado() == null ? "ACTIVO" : metadataEvent.getEstado());
        metadata.setVersion(metadataEvent.getVersion() == null ? 1 : metadataEvent.getVersion());
        metadata.setOwner(metadataEvent.getOwner());
        if (metadata.getCreatedAt() == null) {
            metadata.setCreatedAt(Instant.now());
        }
        metadata.setUpdatedAt(Instant.now());
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

    private Integer asInteger(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.intValue();
        }

        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            log.warn("Could not parse metadata event version '{}'", value);
            return null;
        }
    }
}
