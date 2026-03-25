package demo.infrastructure.adapters.in.rest;

import demo.infrastructure.api.ApiResponse;
import demo.infrastructure.api.HealthResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<HealthResponse>> health() {
        log.info("Gateway health check requested");
        return ResponseEntity.ok(ApiResponse.success(
                new HealthResponse("gateway", "UP"),
                "Gateway is healthy",
                MDC.get("traceId")));
    }
}
