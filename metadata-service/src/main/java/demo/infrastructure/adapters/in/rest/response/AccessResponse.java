package demo.infrastructure.adapters.in.rest.response;

public record AccessResponse(String documentId, String user, boolean allowed) {
}
