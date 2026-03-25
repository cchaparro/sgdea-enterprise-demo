package demo.domain.exception;

public class DocumentAccessDeniedException extends RuntimeException {

    public DocumentAccessDeniedException(String documentId, String user) {
        super("User '%s' does not have access to document '%s'".formatted(user, documentId));
    }
}
