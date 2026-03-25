package demo.application.usecase;

import demo.domain.model.AuditEvent;
import demo.domain.ports.in.ConsumeAuditEventUseCase;
import demo.domain.ports.out.AuditEventRepositoryPort;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumeAuditEventUseCaseImpl implements ConsumeAuditEventUseCase {

    private final AuditEventRepositoryPort auditEventRepositoryPort;

    @Override
    public void consume(Map<String, Object> event) {
        String traceId = event.getOrDefault("traceId", "N/A").toString();
        MDC.put("traceId", traceId);
        try {
            AuditEvent auditEvent = new AuditEvent();
            auditEvent.setEventType(asString(event.get("eventType")));
            auditEvent.setAction(asString(event.get("action")));
            auditEvent.setActor(asString(event.get("actor")));
            auditEvent.setResourceType(asString(event.get("resourceType")));
            auditEvent.setResourceId(asString(event.get("resourceId")));
            auditEvent.setResourceName(asString(event.get("resourceName")));
            auditEvent.setDocumentId(asString(event.get("documentId")));
            auditEvent.setTitle(asString(event.get("title")));
            auditEvent.setFileUrl(asString(event.get("fileUrl")));
            auditEvent.setParentId(asString(event.get("parentId")));
            auditEvent.setOwner(asString(event.get("owner")));
            auditEvent.setTraceId(asString(event.get("traceId")));
            auditEvent.setOccurredAt(parseInstant(event.get("timestamp")));
            auditEvent.setReceivedAt(Instant.now());

            auditEventRepositoryPort.save(auditEvent);
            log.info("AUDIT EVENT -> {}", event);
        } finally {
            MDC.clear();
        }
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
            log.warn("Could not parse audit event timestamp '{}'", value);
            return null;
        }
    }
}
