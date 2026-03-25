package demo.application.usecase;

import demo.domain.exception.MetadataNotFoundException;
import demo.domain.model.Expediente;
import demo.domain.ports.in.ExpedienteUseCase;
import demo.domain.ports.out.ExpedienteRepositoryPort;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpedienteUseCaseImpl implements ExpedienteUseCase {

    private final ExpedienteRepositoryPort expedienteRepositoryPort;

    @Override
    public Expediente create(String codigo, String nombre, String serie, String subserie) {
        log.info("Creating expediente '{}'", codigo);
        Expediente expediente = new Expediente();
        expediente.setId(UUID.randomUUID().toString());
        expediente.setCodigo(codigo);
        expediente.setNombre(nombre);
        expediente.setSerie(serie);
        expediente.setSubserie(subserie);
        expediente.setEstado("ABIERTO");
        expediente.setFechaApertura(Instant.now());
        return expedienteRepositoryPort.save(expediente);
    }

    @Override
    public Expediente getById(String id) {
        return expedienteRepositoryPort.findById(id)
                .orElseThrow(() -> new MetadataNotFoundException(id));
    }
}
