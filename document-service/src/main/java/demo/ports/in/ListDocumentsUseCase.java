package demo.ports.in;

import demo.domain.Document;
import java.util.List;

public interface ListDocumentsUseCase {

    List<Document> listByOwner(String owner);
}
