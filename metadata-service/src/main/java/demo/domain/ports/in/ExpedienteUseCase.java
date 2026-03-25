package demo.domain.ports.in;

import demo.domain.model.Expediente;

public interface ExpedienteUseCase {

    Expediente create(String codigo, String nombre, String serie, String subserie);

    Expediente getById(String id);
}
