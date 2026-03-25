package demo.infrastructure.adapters.in.rest.response;

import demo.domain.model.Expediente;
import java.time.Instant;

public record ExpedienteResponse(
        String id,
        String codigo,
        String nombre,
        String serie,
        String subserie,
        String estado,
        Instant fechaApertura,
        Instant fechaCierre) {

    public static ExpedienteResponse from(Expediente expediente) {
        return new ExpedienteResponse(
                expediente.getId(),
                expediente.getCodigo(),
                expediente.getNombre(),
                expediente.getSerie(),
                expediente.getSubserie(),
                expediente.getEstado(),
                expediente.getFechaApertura(),
                expediente.getFechaCierre());
    }
}
