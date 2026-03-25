
package com.template.domain.ports.in;

import reactor.core.publisher.Flux;

public interface SearchUseCase {
    Flux<String> search(String query, String traceId);
}
