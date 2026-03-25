package demo.application.usecase;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuditEventMessage {

    private String eventType;
    private String action;
    private String actor;
    private String resourceType;
    private String resourceId;
    private String resourceName;
    private String traceId;
    private String timestamp;
}
