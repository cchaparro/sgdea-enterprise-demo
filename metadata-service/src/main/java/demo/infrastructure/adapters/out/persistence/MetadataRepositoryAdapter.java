package demo.infrastructure.adapters.out.persistence;

import demo.domain.model.Metadata;
import demo.domain.ports.out.MetadataRepositoryPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetadataRepositoryAdapter implements MetadataRepositoryPort {

    private final SpringDataMetadataRepository repository;

    @Override
    public Metadata save(Metadata metadata) {
        return repository.save(metadata);
    }

    @Override
    public Optional<Metadata> findById(String documentId) {
        return repository.findById(documentId);
    }
}
