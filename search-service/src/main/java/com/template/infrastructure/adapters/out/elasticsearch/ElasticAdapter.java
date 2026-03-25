
package com.template.infrastructure.adapters.out.elasticsearch;

import com.template.domain.ports.out.SearchRepositoryPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ElasticAdapter implements SearchRepositoryPort {

    @Override
    public Flux<String> search(String query) {
        return Flux.just("Result for: " + query);
    }
}
