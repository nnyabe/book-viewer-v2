package view.book.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

@Configuration
public class DataSourceConfig {

    @Bean
    public HikariDataSource dataSource() {
        SsmClient ssmClient = SsmClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();

        // Fetch username
        String username;
        try {
            username = ssmClient.getParameter(GetParameterRequest.builder()
                    .name("/myapp/rds/username")
                    .build()).parameter().value();
        } catch (Exception e) {
            System.err.println("Failed to fetch dbUsername: " + e.getMessage());
            throw new RuntimeException("SSM fetch failed for dbUsername", e);
        }

        // Fetch password
        String password;
        try {
            password = ssmClient.getParameter(GetParameterRequest.builder()
                    .name("/myapp/rds/password")
                    .withDecryption(true)
                    .build()).parameter().value();
        } catch (Exception e) {
            System.err.println("Failed to fetch dbPassword: " + e.getMessage());
            throw new RuntimeException("SSM fetch failed for dbPassword", e);
        }

        // Configure DataSource
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://myapp-postgres.cjycgc86qpn6.eu-central-1.rds.amazonaws.com:5432/myappdb");
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("org.postgresql.Driver");

        return dataSource;
    }
}