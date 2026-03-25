package demo.infrastructure.api;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String traceId,
        Instant timestamp) {

    public static <T> ApiResponse<T> success(T data, String message, String traceId) {
        return new ApiResponse<>(true, message, data, traceId, Instant.now());
    }
}
