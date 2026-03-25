
package com.template.infrastructure.adapters.in.rest;

import com.template.domain.ports.in.SearchUseCase;
import com.template.infrastructure.adapters.in.rest.response.SearchResponse;
import com.template.infrastructure.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchUseCase useCase;

    @GetMapping
    public Mono<ApiResponse<SearchResponse>> search(
            @RequestParam String q,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId) {
        log.info("Received search request for query '{}'", q);
        return useCase.search(q, traceId)
                .collectList()
                .map(results -> ApiResponse.success(
                        new SearchResponse(q, results),
                        "Search completed",
                        traceId));
    }
}
