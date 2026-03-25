package demo.infrastructure.adapters.out.minio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import demo.ports.out.StoragePort;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioAdapter implements StoragePort {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name:documents}")
    private String bucketName;

    @PostConstruct
    public void init() {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created bucket: {}", bucketName);
            }
        } catch (MinioException | IOException | GeneralSecurityException e) {
            log.error("Error checking/creating bucket: {}", e.getMessage());
        }
    }

    @Override
    public String save(InputStream file, String filename) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .stream(file, -1, 10485760)
                            .build());
            
            log.info("File saved to MinIO: {}/{}", bucketName, filename);
            return String.format("%s/%s", bucketName, filename);
            
        } catch (MinioException | IOException | GeneralSecurityException e) {
            log.error("Error saving file to MinIO: {}", e.getMessage());
            throw new RuntimeException("Failed to save file to storage", e);
        }
    }

}
