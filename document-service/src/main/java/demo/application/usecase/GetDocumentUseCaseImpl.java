package demo.application.usecase;

import demo.domain.Document;
import demo.domain.exception.DocumentAccessDeniedException;
import demo.domain.exception.DocumentNotFoundException;
import demo.ports.in.GetDocumentUseCase;
import demo.ports.out.EventPublisherPort;
import demo.ports.out.MetadataServicePort;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDocumentUseCaseImpl implements GetDocumentUseCase {

    private final MetadataServicePort metadataServicePort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public Document getById(String documentId, String user) {
        Document document = metadataServicePort.getMetadata(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        if (user != null && !user.isBlank() && !metadataServicePort.checkAccess(documentId, user)) {
            throw new DocumentAccessDeniedException(documentId, user);
        }

        if (user != null && !user.isBlank()) {
            eventPublisherPort.publishAudit(AuditEventMessage.builder()
                    .eventType("audit.document.viewed")
                    .action("DOCUMENT_VIEWED")
                    .actor(user)
                    .resourceType("DOCUMENT")
                    .resourceId(document.getId())
                    .resourceName(document.getTitle())
                    .traceId(MDC.get("traceId"))
                    .timestamp(Instant.now().toString())
                    .build());
        }

        return document;
    }
}
