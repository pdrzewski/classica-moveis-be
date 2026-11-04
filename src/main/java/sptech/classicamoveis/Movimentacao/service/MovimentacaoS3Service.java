package sptech.classicamoveis.Movimentacao.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimentacaoS3Service {

    private static final Logger log = LoggerFactory.getLogger(MovimentacaoS3Service.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Value("${aws.s3.enabled:false}")
    private boolean enabled;

    @Value("${aws.access-key-id:}")
    private String accessKeyId;

    @Value("${aws.session-token:}")
    private String sessionToken;

    @Value("${aws.secret-access-key:}")
    private String secretAccessKey;

    @Value("${aws.s3.bucket:}")
    private String bucketName;

    @Value("${aws.s3.prefix:movimentacoes}")
    private String prefix;

    @Value("${aws.s3.region:us-east-1}")
    private String region;

    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    @Value("${aws.s3.force-path-style:false}")
    private boolean forcePathStyle;

    public void enviarMovimentacaoJson(Integer movimentacaoId, Object payload) {
        if (!enabled || movimentacaoId == null || payload == null) {
            return;
        }

        if (bucketName == null || bucketName.isBlank()) {
            throw new IllegalStateException("aws.s3.bucket não configurado");
        }

        String json;
        try {
            json = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Falha ao serializar a movimentação para JSON", e);
        }

        String hoje = LocalDate.now().toString();
        String objectKey = String.format(
                "%s/ano=%s/mes=%s/dia=%s/%s-%s.json",
                prefix,
                hoje.substring(0, 4),
                hoje.substring(5, 7),
                hoje.substring(8, 10),
                movimentacaoId,
                UUID.randomUUID()
        );

        S3Client s3Client = criarClient();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .contentType("application/json")
                            .build(),
                    RequestBody.fromString(json)
            );

            log.info("JSON da movimentação {} enviado ao S3 com sucesso: {}", movimentacaoId, objectKey);
        } catch (S3Exception e) {
            log.error("Falha ao enviar JSON da movimentação {} para o S3", movimentacaoId, e);
            throw new IllegalStateException("Falha ao enviar movimentação para o S3", e);
        } finally {
            s3Client.close();
        }
    }

    private S3Client criarClient() {
        S3ClientBuilder builder = S3Client.builder()
                .region(Region.of(region));

        if (endpoint != null && !endpoint.isBlank()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        if (forcePathStyle) {
            builder.forcePathStyle(true);
        }

        if (accessKeyId != null && !accessKeyId.isBlank() && secretAccessKey != null && !secretAccessKey.isBlank()) {
            if (sessionToken != null && !sessionToken.isBlank()) {
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(accessKeyId, secretAccessKey, sessionToken)
                ));
            } else {
                builder.credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ));
            }
        }

        return builder.build();
    }
}
