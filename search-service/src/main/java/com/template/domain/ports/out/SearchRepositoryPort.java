
package com.template.domain.ports.out;

import com.template.domain.model.DocumentSearchView;
import reactor.core.publisher.Flux;

public interface SearchRepositoryPort {

    void index(DocumentSearchView document);

    Flux<DocumentSearchView> search(String query);

    Flux<DocumentSearchView> searchByOwner(String owner);
}
