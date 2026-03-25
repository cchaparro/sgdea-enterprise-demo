package demo.domain.ports.out;

import demo.domain.model.Metadata;
import java.util.Optional;

public interface MetadataRepositoryPort {

    Metadata save(Metadata metadata);

    Optional<Metadata> findById(String documentId);
}
