package demo.domain.ports.out;

import demo.domain.model.MetadataEvent;

public interface MetadataEventRepositoryPort {

    MetadataEvent save(MetadataEvent event);
}
