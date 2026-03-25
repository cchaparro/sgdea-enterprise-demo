package demo.domain.exception;

public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(String documentId) {
        super("Document '%s' was not found".formatted(documentId));
    }
}
