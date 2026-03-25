
package com.template.application.usecase;

import com.template.domain.ports.in.SearchUseCase;
import com.template.domain.ports.out.SearchRepositoryPort;
import com.template.domain.ports.out.KafkaPort;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
public class SearchUseCaseImpl implements SearchUseCase {

    private final SearchRepositoryPort repository;
    private final KafkaPort kafka;

    @Override
    public Flux<String> search(String query, String traceId) {
        String effectiveTraceId = traceId == null || traceId.isBlank() ? "N/A" : traceId;
        log.info("Executing search for query '{}'", query);
        kafka.send("audit-events", Map.of(
                "eventType", "search.executed",
                "query", query,
                "traceId", effectiveTraceId));
        return repository.search(query);
    }
}
