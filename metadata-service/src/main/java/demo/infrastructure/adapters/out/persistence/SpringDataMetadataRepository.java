package demo.infrastructure.adapters.out.persistence;

import demo.domain.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMetadataRepository extends JpaRepository<Metadata, String> {
}
