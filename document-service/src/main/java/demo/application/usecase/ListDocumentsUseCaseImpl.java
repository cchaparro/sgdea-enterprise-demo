package demo.application.usecase;

import demo.domain.Document;
import demo.ports.in.ListDocumentsUseCase;
import demo.ports.out.MetadataServicePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListDocumentsUseCaseImpl implements ListDocumentsUseCase {

    private final MetadataServicePort metadataServicePort;

    @Override
    public List<Document> listByOwner(String owner) {
        return metadataServicePort.listByOwner(owner);
    }
}
