package view.book.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import java.util.Map;

@Configuration
public class DataSourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    public HikariDataSource dataSource() {
        // Log all AWS environment details
        logAwsEnvironmentDetails();

        SsmClient ssmClient = SsmClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        try {
            // Detailed parameter retrieval with logging
            String username = fetchParameter(ssmClient, "/myapp/rds/username", false);
            String password = fetchParameter(ssmClient, "/myapp/rds/password", true);
            String endpoint = fetchParameter(ssmClient, "/myapp/rds/endpoint", false);
            String port = fetchParameter(ssmClient, "/myapp/rds/port", false);
            String dbName = fetchParameter(ssmClient, "/myapp/rds/dbname", false);

            // Log retrieved values (be careful not to log sensitive info in production)
            logger.info("RDS Connection Details - Endpoint: {}, Port: {}, DBName: {}", endpoint, port, dbName);

            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl(String.format("jdbc:postgresql://%s:%s/%s", endpoint, port, dbName));
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            dataSource.setDriverClassName("org.postgresql.Driver");

            return dataSource;
        } catch (Exception e) {
            logger.error("Failed to create DataSource", e);
            throw new RuntimeException("Could not create DataSource", e);
        }
    }

    private String fetchParameter(SsmClient ssmClient, String parameterName, boolean withDecryption) {
        try {
            logger.info("Attempting to fetch parameter: {}", parameterName);
            GetParameterResponse response = ssmClient.getParameter(request ->
                    request.name(parameterName)
                            .withDecryption(withDecryption)
            );

            String value = response.parameter().value();
            logger.info("Successfully retrieved parameter: {}", parameterName);
            return value;
        } catch (Exception e) {
            logger.error("Error fetching parameter {}: {}", parameterName, e.getMessage(), e);
            throw new RuntimeException("SSM fetch failed for " + parameterName, e);
        }
    }

    private void logAwsEnvironmentDetails() {
        logger.info("AWS Region: {}", Region.EU_CENTRAL_1);
        logger.info("Default Credentials Provider Chain in use");

        // Log environment variables that might be relevant
        Map<String, String> env = System.getenv();
        logger.info("AWS_REGION: {}", env.get("AWS_REGION"));
        logger.info("AWS_DEFAULT_REGION: {}", env.get("AWS_DEFAULT_REGION"));
        logger.info("ECS_CONTAINER_METADATA_URI: {}", env.get("ECS_CONTAINER_METADATA_URI"));
    }
}