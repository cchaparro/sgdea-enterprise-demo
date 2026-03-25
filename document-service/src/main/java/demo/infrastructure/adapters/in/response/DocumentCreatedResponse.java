package demo.infrastructure.adapters.in.response;

public record DocumentCreatedResponse(
        String documentId,
        String title,
        String fileUrl,
        String owner,
        String parentId) {
}
