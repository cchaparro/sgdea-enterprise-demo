package com.template.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentSearchView {

    private String id;
    private String title;
    private String owner;
    private String fileUrl;
    private String parentId;
    private String expedienteId;
    private String tipoDocumental;
    private String estado;
    private Integer version;
    private String traceId;
}
