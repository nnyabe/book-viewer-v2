package view.book;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Configuration
public class SsmConfig {

    @Bean
    public SsmClient ssmClient() {
        return SsmClient.builder()
                .region(Region.EU_CENTRAL_1) // Match your RDS region
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    public String dbUsername(SsmClient ssmClient) {
        GetParameterRequest request = GetParameterRequest.builder()
                .name("/myapp/rds/username")
                .build();
        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }

    @Bean
    public String dbPassword(SsmClient ssmClient) {
        GetParameterRequest request = GetParameterRequest.builder()
                .name("/myapp/rds/password")
                .withDecryption(true) // Required for SecureString
                .build();
        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }
}