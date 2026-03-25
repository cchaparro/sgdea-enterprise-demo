package demo.ports.out;

public interface EventPublisherPort {
    void publishMetadata(Object event);
    void publishAudit(Object event);
}
