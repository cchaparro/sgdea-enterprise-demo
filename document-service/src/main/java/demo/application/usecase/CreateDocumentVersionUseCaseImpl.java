package demo.application.usecase;

import demo.domain.Document;
import demo.domain.exception.DocumentAccessDeniedException;
import demo.domain.exception.DocumentNotFoundException;
import demo.ports.in.CreateDocumentCommand;
import demo.ports.in.CreateDocumentVersionUseCase;
import demo.ports.out.EventPublisherPort;
import demo.ports.out.LogPort;
import demo.ports.out.MetadataServicePort;
import demo.ports.out.StoragePort;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDocumentVersionUseCaseImpl implements CreateDocumentVersionUseCase {

    private final StoragePort storage;
    private final EventPublisherPort publisher;
    private final LogPort log;
    private final MetadataServicePort metadataService;

    @Override
    public Document createVersion(String documentId, CreateDocumentCommand cmd) {
        Document currentDocument = metadataService.getMetadata(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        if (!currentDocument.getOwner().equals(cmd.getOwner())) {
            throw new DocumentAccessDeniedException(documentId, cmd.getOwner());
        }

        Integer nextVersion = (currentDocument.getVersion() == null ? 1 : currentDocument.getVersion()) + 1;
        String title = cmd.getTitle() == null || cmd.getTitle().isBlank() ? currentDocument.getTitle() : cmd.getTitle();
        String estado = cmd.getEstado() == null || cmd.getEstado().isBlank() ? currentDocument.getEstado() : cmd.getEstado();
        String fileUrl = storage.save(cmd.getFile(), buildObjectName(documentId, nextVersion, title));

        log.info("Creating version '%s' for document '%s'".formatted(nextVersion, documentId));

        DocumentEvent event = DocumentEvent.builder()
                .eventType("document.version.created")
                .documentId(documentId)
                .title(title)
                .fileUrl(fileUrl)
                .owner(currentDocument.getOwner())
                .parentId(currentDocument.getParentId())
                .expedienteId(currentDocument.getExpedienteId())
                .tipoDocumental(currentDocument.getTipoDocumental())
                .estado(estado)
                .version(nextVersion)
                .timestamp(Instant.now().toString())
                .traceId(MDC.get("traceId"))
                .build();

        AuditEventMessage auditEvent = AuditEventMessage.builder()
                .eventType("audit.document.version.created")
                .action("DOCUMENT_VERSION_CREATED")
                .actor(cmd.getOwner())
                .resourceType("DOCUMENT")
                .resourceId(documentId)
                .resourceName(title)
                .traceId(MDC.get("traceId"))
                .timestamp(Instant.now().toString())
                .build();

        metadataService.saveMetadata(event);
        publisher.publishAudit(auditEvent);

        return Document.builder()
                .id(documentId)
                .title(title)
                .fileUrl(fileUrl)
                .owner(currentDocument.getOwner())
                .parentId(currentDocument.getParentId())
                .expedienteId(currentDocument.getExpedienteId())
                .tipoDocumental(currentDocument.getTipoDocumental())
                .estado(estado)
                .version(nextVersion)
                .build();
    }

    private String buildObjectName(String documentId, Integer version, String title) {
        String safeTitle = (title == null || title.isBlank()) ? documentId : title;
        return "%s/v%s/%s".formatted(documentId, version, safeTitle);
    }
}
