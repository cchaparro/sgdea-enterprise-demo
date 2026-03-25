package demo.domain.ports.in;

import java.util.Map;

public interface ConsumeMetadataEventUseCase {

    void consume(Map<String, Object> event);
}
