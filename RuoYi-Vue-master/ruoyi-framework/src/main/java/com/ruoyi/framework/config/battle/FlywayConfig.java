package com.ruoyi.framework.config.battle;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author hongjiasen
 */
@Configuration
public class FlywayConfig {

    private DataSource dataSource;

    public FlywayConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void migrate() {
        ClassicConfiguration classicConfiguration = new ClassicConfiguration();
        classicConfiguration.setDataSource(dataSource);
        classicConfiguration.setBaselineOnMigrate(true);
        Flyway flyway = new Flyway(classicConfiguration);
        flyway.migrate();
    }
}
