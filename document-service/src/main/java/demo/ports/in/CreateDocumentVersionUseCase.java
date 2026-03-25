package demo.ports.in;

import demo.domain.Document;

public interface CreateDocumentVersionUseCase {

    Document createVersion(String documentId, CreateDocumentCommand command);
}
