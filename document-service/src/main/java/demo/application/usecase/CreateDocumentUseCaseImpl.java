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
            log.info("Creating document: " + documentId);

            String fileUrl = storage.save(cmd.getFile(), documentId);

            DocumentEvent event = DocumentEvent.builder()
                    .eventType("document.created")
                    .documentId(documentId)
                    .title(cmd.getTitle())
                    .fileUrl(fileUrl)
                    .owner(cmd.getOwner())
                    .parentId(cmd.getParentId())
                    .timestamp(Instant.now().toString())
                    .traceId(MDC.get("traceId"))
                    .build();

            metadataService.saveMetadata(event);
            publisher.publishAudit(event);
            
            return Document.builder()
                    .id(documentId)
                    .title(cmd.getTitle())
                    .fileUrl(fileUrl)
                    .owner(cmd.getOwner())
                    .parentId(cmd.getParentId())
                    .build();

        } catch (Exception e) {
            log.error("Error creating document", e);
            throw e;
        }
    }

}
