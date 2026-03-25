package demo.infrastructure.adapters.in.response;

import demo.domain.Document;

public record DocumentResponse(
        String documentId,
        String title,
        String fileUrl,
        String owner,
        String parentId,
        String expedienteId,
        String tipoDocumental,
        String estado,
        Integer version) {

    public static DocumentResponse from(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getTitle(),
                document.getFileUrl(),
                document.getOwner(),
                document.getParentId(),
                document.getExpedienteId(),
                document.getTipoDocumental(),
                document.getEstado(),
                document.getVersion());
    }
}
