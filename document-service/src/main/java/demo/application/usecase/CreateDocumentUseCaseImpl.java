package demo.application.usecase;

import java.time.Instant;
import java.util.UUID;

import demo.domain.Document;
import demo.ports.in.CreateDocumentCommand;
import demo.ports.in.CreateDocumentUseCase;
import demo.ports.out.EventPublisherPort;
import demo.ports.out.LogPort;
import demo.ports.out.MetadataServicePort;
import demo.ports.out.StoragePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDocumentUseCaseImpl implements CreateDocumentUseCase {

    private final StoragePort storage;
    private final EventPublisherPort publisher;
    private final LogPort log;
    private final MetadataServicePort metadataService;

    @Override
    public Document create(CreateDocumentCommand cmd) {
        try {
            String documentId = UUID.randomUUID().toString();
            Integer version = cmd.getVersion() == null ? 1 : cmd.getVersion();
            log.info("Creating document: " + documentId);

            String fileUrl = storage.save(cmd.getFile(), buildObjectName(documentId, version, cmd.getTitle()));

            DocumentEvent event = DocumentEvent.builder()
                    .eventType("document.created") 
                    .documentId(documentId)
                    .title(cmd.getTitle())
                    .fileUrl(fileUrl)
                    .owner(cmd.getOwner())
                    .parentId(cmd.getParentId())
                    .expedienteId(cmd.getExpedienteId())
                    .tipoDocumental(cmd.getTipoDocumental())
                    .estado(cmd.getEstado())
                    .version(version)
                    .timestamp(Instant.now().toString())
                    .traceId(MDC.get("traceId"))
                    .build();

            AuditEventMessage auditEvent = AuditEventMessage.builder()
                    .eventType("audit.document.created")
                    .action("DOCUMENT_CREATED")
                    .actor(cmd.getOwner())
                    .resourceType("DOCUMENT")
                    .resourceId(documentId)
                    .resourceName(cmd.getTitle())
                    .traceId(MDC.get("traceId"))
                    .timestamp(Instant.now().toString())
                    .build();

            metadataService.saveMetadata(event);
            publisher.publishAudit(auditEvent);
            
            return Document.builder()
                    .id(documentId)
                    .title(cmd.getTitle())
                    .fileUrl(fileUrl)
                    .owner(cmd.getOwner())
                    .parentId(cmd.getParentId())
                    .expedienteId(cmd.getExpedienteId())
                    .tipoDocumental(cmd.getTipoDocumental())
                    .estado(cmd.getEstado())
                    .version(version)
                    .build();

        } catch (Exception e) {
            log.error("Error creating document", e);
            throw e;
        }
    }

    private String buildObjectName(String documentId, Integer version, String title) {
        String safeTitle = (title == null || title.isBlank()) ? documentId : title;
        return "%s/v%s/%s".formatted(documentId, version, safeTitle);
    }

}
