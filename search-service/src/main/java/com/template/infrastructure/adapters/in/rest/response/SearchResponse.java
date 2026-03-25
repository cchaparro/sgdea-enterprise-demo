package com.template.infrastructure.adapters.in.rest.response;

import com.template.domain.model.DocumentSearchView;
import java.util.List;

public record SearchResponse(String query, List<DocumentSearchView> results) {
}
