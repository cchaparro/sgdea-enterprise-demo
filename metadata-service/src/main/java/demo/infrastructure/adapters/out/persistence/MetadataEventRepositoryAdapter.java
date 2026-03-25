package demo.infrastructure.adapters.out.persistence;

import demo.domain.model.MetadataEvent;
import demo.domain.ports.out.MetadataEventRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MetadataEventRepositoryAdapter implements MetadataEventRepositoryPort {

    private final SpringDataMetadataEventRepository repository;

    @Override
    public MetadataEvent save(MetadataEvent event) {
        return repository.save(event);
    }

    @Override
    public List<MetadataEvent> findByDocumentId(String documentId) {
        return repository.findByDocumentIdOrderByVersionAscReceivedAtAsc(documentId);
    }
}
