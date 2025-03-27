package view.book.config;

import static org.junit.jupiter.api.Assertions.*;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
class DataSourceConfigTest {
    @Bean
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/testdb"); // Use a local test DB
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpass");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

}