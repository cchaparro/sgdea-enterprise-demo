package demo.infrastructure.api;

import demo.domain.exception.MetadataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MetadataNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorDetail>> handleNotFound(MetadataNotFoundException ex) {
        log.warn("Metadata not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        ex.getMessage(),
                        new ErrorDetail("METADATA_NOT_FOUND", ex.getMessage()),
                        MDC.get("traceId")));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorDetail>> handleGeneric(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Unexpected error",
                        new ErrorDetail("INTERNAL_ERROR", ex.getMessage()),
                        MDC.get("traceId")));
    }
}
