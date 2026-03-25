package demo.application.usecase;

import demo.domain.Document;
import demo.domain.exception.DocumentAccessDeniedException;
import demo.domain.exception.DocumentNotFoundException;
import demo.ports.in.DownloadDocumentUseCase;
import demo.ports.out.EventPublisherPort;
import demo.ports.out.MetadataServicePort;
import demo.ports.out.StoragePort;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DownloadDocumentUseCaseImpl implements DownloadDocumentUseCase {

    private final MetadataServicePort metadataServicePort;
    private final StoragePort storagePort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public String download(String documentId, String user) {
        Document document = metadataServicePort.getMetadata(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(documentId));

        if (!metadataServicePort.checkAccess(documentId, user)) {
            throw new DocumentAccessDeniedException(documentId, user);
        }

        eventPublisherPort.publishAudit(AuditEventMessage.builder()
                .eventType("audit.document.download-url-generated")
                .action("DOCUMENT_DOWNLOAD_URL_GENERATED")
                .actor(user)
                .resourceType("DOCUMENT")
                .resourceId(document.getId())
                .resourceName(document.getTitle())
                .traceId(MDC.get("traceId"))
                .timestamp(Instant.now().toString())
                .build());

        return storagePort.generatePresignedUrl(extractObjectName(document));
    }

    private String extractObjectName(Document document) {
        String fileUrl = document.getFileUrl();
        if (fileUrl == null || fileUrl.isBlank()) {
            return document.getId();
        }

        int slashIndex = fileUrl.indexOf('/');
        return slashIndex >= 0 ? fileUrl.substring(slashIndex + 1) : fileUrl;
    }
}
