package demo.infrastructure.adapters.in.rest.request;

public record CreateExpedienteRequest(
        String codigo,
        String nombre,
        String serie,
        String subserie) {
}
