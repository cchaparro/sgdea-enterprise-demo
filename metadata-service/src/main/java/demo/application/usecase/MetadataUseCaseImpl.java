package demo.application.usecase;

import demo.domain.exception.MetadataNotFoundException;
import demo.domain.model.Metadata;
import demo.domain.ports.in.MetadataUseCase;
import demo.domain.ports.out.MetadataRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataUseCaseImpl implements MetadataUseCase {

    private final MetadataRepositoryPort metadataRepositoryPort;

    @Override
    public Metadata create(String documentId, String owner) {
        log.info("Creating metadata for document '{}'", documentId);
        Metadata metadata = new Metadata();
        metadata.setDocumentId(documentId);
        metadata.setOwner(owner);
        return metadataRepositoryPort.save(metadata);
    }

    @Override
    public Metadata getById(String documentId) {
        log.info("Fetching metadata for document '{}'", documentId);
        return metadataRepositoryPort.findById(documentId)
                .orElseThrow(() -> new MetadataNotFoundException(documentId));
    }

    @Override
    public Metadata grantAccess(String documentId, String user) {
        log.info("Granting access to user '{}' for document '{}'", user, documentId);
        Metadata metadata = getById(documentId);
        if (!metadata.getAllowedUsers().contains(user)) {
            metadata.getAllowedUsers().add(user);
        }
        return metadataRepositoryPort.save(metadata);
    }

    @Override
    public boolean hasAccess(String documentId, String user) {
        log.info("Checking access for user '{}' on document '{}'", user, documentId);
        return metadataRepositoryPort.findById(documentId)
                .map(metadata -> metadata.getOwner().equals(user) || metadata.getAllowedUsers().contains(user))
                .orElse(false);
    }
}
