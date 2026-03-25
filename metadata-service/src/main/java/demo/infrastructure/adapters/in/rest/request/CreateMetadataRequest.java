package demo.infrastructure.adapters.in.rest.request;

public record CreateMetadataRequest(
        String documentId,
        String owner,
        String title,
        String fileUrl,
        String parentId,
        String expedienteId,
        String tipoDocumental,
        String estado,
        Integer version) {
}
