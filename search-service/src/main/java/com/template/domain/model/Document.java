
package com.template.domain.model;

import lombok.*;
import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class Document {
    private String id;
    private String title;
    private String content;
    private Map<String, Object> metadata;
    private List<String> aclFlat;
}
