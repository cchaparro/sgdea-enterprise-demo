package demo.infrastructure.adapters.in.response;

public record DocumentDownloadResponse(
        String documentId,
        String downloadUrl,
        String expiresIn) {
}
