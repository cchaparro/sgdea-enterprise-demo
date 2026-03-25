package demo.ports.out;

import demo.application.usecase.DocumentEvent;
import demo.domain.Document;
import java.util.List;
import java.util.Optional;

public interface MetadataServicePort {

    void saveMetadata(DocumentEvent event);

    boolean checkAccess(String documentId, String user);

    Optional<Document> getMetadata(String documentId);

    List<Document> listByOwner(String owner);
}
