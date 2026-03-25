package demo.domain.ports.in;

import demo.domain.model.Metadata;

public interface MetadataUseCase {

    Metadata create(String documentId, String owner);

    Metadata getById(String documentId);

    Metadata grantAccess(String documentId, String user);

    boolean hasAccess(String documentId, String user);
}
