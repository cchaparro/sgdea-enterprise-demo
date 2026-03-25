package demo.ports.in;

import demo.domain.Document;

public interface GetDocumentUseCase {

    Document getById(String documentId, String user);
}
