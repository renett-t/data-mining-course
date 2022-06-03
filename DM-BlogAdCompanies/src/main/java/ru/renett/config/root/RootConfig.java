package ru.renett.config.root;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.renett.Constants;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@ComponentScan({"ru.renett.repository.database", "ru.renett.services.crawl.serialization", "ru.renett.api", "ru.renett.repository.net"})
@PropertySource("classpath:app.properties")
public class RootConfig {

    @Resource
    private Environment environment;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(environment.getProperty("db.url"));
        hikariConfig.setDriverClassName(environment.getProperty("db.driver"));
        hikariConfig.setUsername(environment.getProperty("db.username"));
        hikariConfig.setPassword(environment.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("db.max-pool-size")));
        return hikariConfig;
    }

    @Bean
    public DataSource dataSource(HikariConfig config) {
        return new HikariDataSource(config);
    }

    @Bean
    public String youtubeApiKey() {
        return environment.getProperty("youtube.api-key");
    }

    @Bean
    public String whoisApiKey() {
        return environment.getProperty("whois.api-key");
    }

}
