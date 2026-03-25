package demo.infrastructure.adapters.in.rest;

import demo.domain.model.Expediente;
import demo.domain.ports.in.ExpedienteUseCase;
import demo.infrastructure.adapters.in.rest.request.CreateExpedienteRequest;
import demo.infrastructure.adapters.in.rest.response.ExpedienteResponse;
import demo.infrastructure.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/expedientes")
@RequiredArgsConstructor
public class ExpedienteController {

    private final ExpedienteUseCase expedienteUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpedienteResponse>> create(@RequestBody CreateExpedienteRequest request) {
        log.info("Creating expediente with code '{}'", request.codigo());
        Expediente expediente = expedienteUseCase.create(
                request.codigo(),
                request.nombre(),
                request.serie(),
                request.subserie());
        return ResponseEntity.ok(ApiResponse.success(
                ExpedienteResponse.from(expediente),
                "Expediente created",
                MDC.get("traceId")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpedienteResponse>> getById(@PathVariable String id) {
        Expediente expediente = expedienteUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(
                ExpedienteResponse.from(expediente),
                "Expediente fetched",
                MDC.get("traceId")));
    }
}
