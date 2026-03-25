
package com.template.domain.ports.in;

import com.template.domain.model.DocumentSearchView;
import reactor.core.publisher.Flux;

public interface SearchUseCase {
    Flux<DocumentSearchView> search(String query, String traceId);

    Flux<DocumentSearchView> searchByOwner(String owner, String traceId);
}
