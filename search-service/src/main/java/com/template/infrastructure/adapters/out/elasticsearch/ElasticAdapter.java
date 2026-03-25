package com.template.infrastructure.adapters.out.elasticsearch;

import com.template.domain.model.DocumentSearchView;
import com.template.domain.ports.out.SearchRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class ElasticAdapter implements SearchRepositoryPort {

    private final ElasticsearchOperations operations;

    @Override
    public void index(DocumentSearchView document) {
        operations.save(SearchDocumentEntity.from(document));
    }

    @Override
    public Flux<DocumentSearchView> search(String query) {
        Criteria criteria = new Criteria("title").contains(query)
                .or(new Criteria("owner").contains(query))
                .or(new Criteria("expedienteId").contains(query))
                .or(new Criteria("tipoDocumental").contains(query))
                .or(new Criteria("estado").contains(query))
                .or(new Criteria("parentId").contains(query));
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);

        List<DocumentSearchView> results = operations.search(criteriaQuery, SearchDocumentEntity.class)
                .stream()
                .map(SearchHit::getContent)
                .map(SearchDocumentEntity::toView)
                .toList();
        return Flux.fromIterable(results);
    }

    @Override
    public Flux<DocumentSearchView> searchByOwner(String owner) {
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("owner").is(owner));

        List<DocumentSearchView> results = operations.search(criteriaQuery, SearchDocumentEntity.class)
                .stream()
                .map(SearchHit::getContent)
                .map(SearchDocumentEntity::toView)
                .toList();
        return Flux.fromIterable(results);
    }
}
