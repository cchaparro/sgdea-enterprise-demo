package demo.application.usecase;

import demo.domain.exception.MetadataNotFoundException;
import demo.domain.model.Metadata;
import demo.domain.model.MetadataEvent;
import demo.domain.ports.in.MetadataUseCase;
import demo.domain.ports.out.MetadataEventRepositoryPort;
import demo.domain.ports.out.MetadataRepositoryPort;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MetadataUseCaseImpl implements MetadataUseCase {

    private final MetadataRepositoryPort metadataRepositoryPort;
    private final MetadataEventRepositoryPort metadataEventRepositoryPort;

    @Override
    public Metadata create(String documentId, String owner) {
        log.info("Creating metadata for document '{}'", documentId);
        Metadata metadata = new Metadata();
        metadata.setDocumentId(documentId);
        metadata.setOwner(owner);
        metadata.setEstado("ACTIVO");
        metadata.setVersion(1);
        metadata.setCreatedAt(Instant.now());
        metadata.setUpdatedAt(Instant.now());
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
        metadata.setUpdatedAt(Instant.now());
        return metadataRepositoryPort.save(metadata);
    }

    @Override
    public boolean hasAccess(String documentId, String user) {
        log.info("Checking access for user '{}' on document '{}'", user, documentId);
        return metadataRepositoryPort.findById(documentId)
                .map(metadata -> metadata.getOwner().equals(user) || metadata.getAllowedUsers().contains(user))
                .orElse(false);
    }

    @Override
    public List<Metadata> listByOwner(String owner) {
        log.info("Listing metadata for owner '{}'", owner);
        return metadataRepositoryPort.findByOwner(owner);
    }

    @Override
    public List<MetadataEvent> listVersions(String documentId) {
        log.info("Listing versions for document '{}'", documentId);
        getById(documentId);
        return metadataEventRepositoryPort.findByDocumentId(documentId);
    }
}
