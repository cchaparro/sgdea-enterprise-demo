package demo.infrastructure.adapters.in.rest;

import demo.application.usecase.AuditEventMessage;
import demo.domain.model.Metadata;
import demo.domain.ports.in.MetadataUseCase;
import demo.infrastructure.adapters.in.rest.request.CreateMetadataRequest;
import demo.infrastructure.adapters.in.rest.request.GrantAccessRequest;
import demo.infrastructure.adapters.in.rest.response.AccessResponse;
import demo.infrastructure.adapters.in.rest.response.MetadataResponse;
import demo.infrastructure.adapters.in.rest.response.MetadataVersionResponse;
import demo.infrastructure.api.ApiResponse;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataUseCase metadataUseCase;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public ResponseEntity<ApiResponse<MetadataResponse>> create(@RequestBody CreateMetadataRequest request) {
        log.info("Received metadata creation request for document '{}'", request.documentId());
        Metadata metadata = metadataUseCase.create(request.documentId(), request.owner());
        return ResponseEntity.ok(ApiResponse.success(
                MetadataResponse.from(metadata),
                "Metadata created",
                MDC.get("traceId")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MetadataResponse>> getById(@PathVariable String id) {
        Metadata metadata = metadataUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(
                MetadataResponse.from(metadata),
                "Metadata fetched",
                MDC.get("traceId")));
    }

    @GetMapping("/owner/{owner}")
    public ResponseEntity<ApiResponse<List<MetadataResponse>>> listByOwner(@PathVariable String owner) {
        List<MetadataResponse> response = metadataUseCase.listByOwner(owner)
                .stream()
                .map(MetadataResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Metadata list fetched",
                MDC.get("traceId")));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<ApiResponse<List<MetadataVersionResponse>>> listVersions(@PathVariable String id) {
        List<MetadataVersionResponse> response = metadataUseCase.listVersions(id)
                .stream()
                .map(MetadataVersionResponse::from)
                .toList();
        return ResponseEntity.ok(ApiResponse.success(
                response,
                "Metadata versions fetched",
                MDC.get("traceId")));
    }

    @PostMapping("/{id}/acl")
    public ResponseEntity<ApiResponse<MetadataResponse>> acl(@PathVariable String id, @RequestBody GrantAccessRequest request) {
        Metadata metadata = metadataUseCase.grantAccess(id, request.user());
        kafkaTemplate.send("audit-events", AuditEventMessage.builder()
                .eventType("audit.document.access-granted")
                .action("DOCUMENT_ACCESS_GRANTED")
                .actor(request.grantedBy() == null || request.grantedBy().isBlank() ? "SYSTEM" : request.grantedBy())
                .resourceType("DOCUMENT")
                .resourceId(id)
                .resourceName(metadata.getTitle())
                .traceId(MDC.get("traceId"))
                .timestamp(Instant.now().toString())
                .build());
        return ResponseEntity.ok(ApiResponse.success(
                MetadataResponse.from(metadata),
                "Access granted",
                MDC.get("traceId")));
    }

    @GetMapping("/{id}/access/{user}")
    public ResponseEntity<ApiResponse<AccessResponse>> access(@PathVariable String id, @PathVariable String user) {
        boolean allowed = metadataUseCase.hasAccess(id, user);
        return ResponseEntity.ok(ApiResponse.success(
                new AccessResponse(id, user, allowed),
                "Access check completed",
                MDC.get("traceId")));
    }
}
