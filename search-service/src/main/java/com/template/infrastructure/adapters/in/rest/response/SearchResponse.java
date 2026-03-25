package com.template.infrastructure.adapters.in.rest.response;

import java.util.List;

public record SearchResponse(String query, List<String> results) {
}
