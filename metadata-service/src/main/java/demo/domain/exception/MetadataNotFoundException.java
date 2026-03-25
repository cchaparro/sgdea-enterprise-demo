package demo.domain.exception;

public class MetadataNotFoundException extends RuntimeException {

    public MetadataNotFoundException(String documentId) {
        super("Metadata not found for documentId: " + documentId);
    }
}
