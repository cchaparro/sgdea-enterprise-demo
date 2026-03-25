package demo.infrastructure.adapters.in.rest.response;

import demo.domain.model.Metadata;
import java.util.List;

public record MetadataResponse(String documentId, String owner, List<String> allowedUsers) {

    public static MetadataResponse from(Metadata metadata) {
        return new MetadataResponse(metadata.getDocumentId(), metadata.getOwner(), metadata.getAllowedUsers());
    }
}
