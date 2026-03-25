
package com.template.infrastructure.config;

import com.template.infrastructure.api.TraceIdHolder;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingFilter implements WebFilter {

    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String traceId = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(TRACE_ID_HEADER))
                .filter(value -> !value.isBlank())
                .orElse(UUID.randomUUID().toString());

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate().header(TRACE_ID_HEADER, traceId).build())
                .build();

        mutatedExchange.getResponse().getHeaders().set(TRACE_ID_HEADER, traceId);
        MDC.put("traceId", traceId);
        TraceIdHolder.setTraceId(traceId);
        log.info("Incoming request: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI().getPath());

        return chain.filter(mutatedExchange)
                .doFinally(signal -> {
                    log.info("Completed request: {} {} -> {}", exchange.getRequest().getMethod(),
                            exchange.getRequest().getURI().getPath(),
                            exchange.getResponse().getStatusCode());
                    MDC.clear();
                    TraceIdHolder.clear();
                });
    }
}
