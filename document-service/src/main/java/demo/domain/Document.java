package demo.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class Document {
    private String id;
    private String title;
    private String fileUrl;
    private String owner;
    private String parentId;
}