
package com.template.domain.ports.out;

import reactor.core.publisher.Flux;

public interface SearchRepositoryPort {
    Flux<String> search(String query);
}
