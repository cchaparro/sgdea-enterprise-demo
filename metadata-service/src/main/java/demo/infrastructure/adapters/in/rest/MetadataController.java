package demo.infrastructure.adapters.in.rest;

import demo.domain.model.Metadata;
import demo.domain.ports.in.MetadataUseCase;
import demo.infrastructure.adapters.in.rest.request.CreateMetadataRequest;
import demo.infrastructure.adapters.in.rest.request.GrantAccessRequest;
import demo.infrastructure.adapters.in.rest.response.AccessResponse;
import demo.infrastructure.adapters.in.rest.response.MetadataResponse;
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
@RequestMapping("/metadata")
@RequiredArgsConstructor
public class MetadataController {

    private final MetadataUseCase metadataUseCase;

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

    @PostMapping("/{id}/acl")
    public ResponseEntity<ApiResponse<MetadataResponse>> acl(@PathVariable String id, @RequestBody GrantAccessRequest request) {
        Metadata metadata = metadataUseCase.grantAccess(id, request.user());
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
