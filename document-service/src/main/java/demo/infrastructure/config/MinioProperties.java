package demo.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "minio")
@Getter @Setter
public class MinioProperties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}