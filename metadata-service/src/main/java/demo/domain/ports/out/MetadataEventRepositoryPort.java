package demo.domain.ports.out;

import demo.domain.model.MetadataEvent;
import java.util.List;

public interface MetadataEventRepositoryPort {

    MetadataEvent save(MetadataEvent event);

    List<MetadataEvent> findByDocumentId(String documentId);
}
