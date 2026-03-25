package demo.infrastructure.adapters.in.rest.response;

import demo.domain.model.Metadata;
import java.time.Instant;
import java.util.List;

public record MetadataResponse(
        String documentId,
        String owner,
        String title,
        String fileUrl,
        String parentId,
        String expedienteId,
        String tipoDocumental,
        String estado,
        Integer version,
        Instant createdAt,
        Instant updatedAt,
        List<String> allowedUsers) {

    public static MetadataResponse from(Metadata metadata) {
        return new MetadataResponse(
                metadata.getDocumentId(),
                metadata.getOwner(),
                metadata.getTitle(),
                metadata.getFileUrl(),
                metadata.getParentId(),
                metadata.getExpedienteId(),
                metadata.getTipoDocumental(),
                metadata.getEstado(),
                metadata.getVersion(),
                metadata.getCreatedAt(),
                metadata.getUpdatedAt(),
                metadata.getAllowedUsers());
    }
}
