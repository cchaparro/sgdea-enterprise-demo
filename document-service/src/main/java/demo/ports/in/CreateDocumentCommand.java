package demo.ports.in;

import java.io.InputStream;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class CreateDocumentCommand {
    private String title;
    private String owner;
    private String parentId;
    private String expedienteId;
    private String tipoDocumental;
    private String estado;
    private Integer version;
    private InputStream file;
}
