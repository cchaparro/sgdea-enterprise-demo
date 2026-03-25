package demo.application.usecase;

import lombok.Builder;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter @Builder
public class DocumentEvent {


     private String eventType;
    private String documentId;
    private String title;
    private String fileUrl;
     private String parentId;
    private String timestamp;
    private String owner;
    private String traceId;
}
