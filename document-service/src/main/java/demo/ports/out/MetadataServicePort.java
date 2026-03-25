package demo.ports.out;

import demo.application.usecase.DocumentEvent;

public interface MetadataServicePort {

    void saveMetadata(DocumentEvent event);
    boolean checkAccess(String documentId, String user);
}
