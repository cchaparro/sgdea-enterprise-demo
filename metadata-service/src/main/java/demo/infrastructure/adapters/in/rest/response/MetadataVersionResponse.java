package demo.infrastructure.adapters.in.rest.response;

import demo.domain.model.MetadataEvent;
import java.time.Instant;

public record MetadataVersionResponse(
        String documentId,
        Integer version,
        String title,
        String fileUrl,
        String estado,
        String owner,
        Instant occurredAt) {

    public static MetadataVersionResponse from(MetadataEvent event) {
        return new MetadataVersionResponse(
                event.getDocumentId(),
                event.getVersion(),
                event.getTitle(),
                event.getFileUrl(),
                event.getEstado(),
                event.getOwner(),
                event.getOccurredAt());
    }
}
