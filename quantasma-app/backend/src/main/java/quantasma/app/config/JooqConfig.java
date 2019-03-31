package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@Slf4j
@Profile("!mock")
public class JooqConfig {

    private final String url;
    private final String username;
    private final String password;

    public JooqConfig(@Value("${spring.datasource.url}") String url,
                      @Value("${spring.datasource.username}") String username,
                      @Value("${spring.datasource.password}") String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Bean
    public Connection connection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    @Bean
    public DSLContext dslContext() throws SQLException {
        return DSL.using(connection(), SQLDialect.POSTGRES);
    }

    @Bean
    public org.jooq.Configuration configuration() throws SQLException {
        return new DefaultConfiguration().derive(connection());
    }
}
