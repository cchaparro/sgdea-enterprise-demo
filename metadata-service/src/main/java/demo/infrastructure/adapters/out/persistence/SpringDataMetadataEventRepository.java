package demo.infrastructure.adapters.out.persistence;

import demo.domain.model.MetadataEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMetadataEventRepository extends JpaRepository<MetadataEvent, Long> {
}
