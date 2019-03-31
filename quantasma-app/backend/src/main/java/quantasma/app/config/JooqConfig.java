package quantasma.app.config;

import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
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
    private final String name;
    private final String username;
    private final String password;

    public JooqConfig(@Value("spring.datasource.url") String url,
                      @Value("spring.datasource.name") String name,
                      @Value("spring.datasource.username") String username,
                      @Value("spring.datasource.passwrod") String password) {
        this.url = url;
        this.name = name;
        this.username = username;
        this.password = password;
    }

    @Bean
    public Connection connection() throws SQLException {
        return DriverManager.getConnection(url + name, username, password);
    }

    @Bean
    public DSLContext dslContext(Connection conn) {
        return DSL.using(conn, SQLDialect.POSTGRES);
    }

}
