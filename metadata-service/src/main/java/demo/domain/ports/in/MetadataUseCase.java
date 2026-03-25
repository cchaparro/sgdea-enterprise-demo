package demo.domain.ports.in;

import demo.domain.model.Metadata;
import demo.domain.model.MetadataEvent;
import java.util.List;

public interface MetadataUseCase {

    Metadata create(String documentId, String owner);

    Metadata getById(String documentId);

    Metadata grantAccess(String documentId, String user);

    boolean hasAccess(String documentId, String user);

    List<Metadata> listByOwner(String owner);

    List<MetadataEvent> listVersions(String documentId);
}
