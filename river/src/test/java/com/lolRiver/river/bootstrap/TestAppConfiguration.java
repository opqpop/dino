package com.lolRiver.river.bootstrap;

import com.googlecode.flyway.core.Flyway;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

@Configuration
@Import(AppConfiguration.class)
public class TestAppConfiguration {


    @Bean
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost/river_test?zeroDateTimeBehavior=convertToNull&autoReconnect" +
                              "=true");
        dataSource.setUser("river_test");
        dataSource.setPassword("river_test");
        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(20);
        dataSource.setMaxStatements(50);
        dataSource.setIdleConnectionTestPeriod(3000);
        dataSource.setLoginTimeout(300);

        return dataSource;
    }

    // must have a separate bean for running the close method on the data source, since ComboPooledDataSource is final
    @Bean
    public DataSourceCloser dataSourceCloser() {
        return new DataSourceCloser();
    }

    public class DataSourceCloser {
        @Autowired
        private ComboPooledDataSource dataSource;

        @PreDestroy
        public void teardown() {
            dataSource.close();
        }
    }

    // separate wrapper for Flyway for database migrations - Flyway needs to know where it's configuration file is
    @Bean
    public FlywayWrapper flyway() {
        return new FlywayWrapper();
    }

    public class FlywayWrapper extends Flyway {

        @Autowired
        private DataSource dataSource;

        @PostConstruct
        public void setup() {
            setDataSource(dataSource);
            setLocations("filesystem:src/main/resources/db/migration");
            migrate();
        }
    }
}