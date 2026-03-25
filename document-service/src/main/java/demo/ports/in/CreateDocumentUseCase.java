package demo.ports.in;

import demo.domain.Document;

public interface CreateDocumentUseCase {
    Document create(CreateDocumentCommand command);
}
