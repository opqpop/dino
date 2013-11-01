package com.lolRiver.river.bootstrap;

import com.googlecode.flyway.core.Flyway;
import com.lolRiver.config.ConfigMap;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

/**
 * Spring application configuration bootstapping.
 */
@Configuration
// Automatically create singleton instances of classes in the com.lolRiver package annotated with @Component,
// @Controller, @Repository, etc.
@ComponentScan(basePackages = {"com.lolRiver"})
@EnableWebMvc
public class AppConfiguration {

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {
        InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setPrefix("/WEB-INF/views/");
        internalResourceViewResolver.setSuffix(".jsp");
        return internalResourceViewResolver;
    }

    // create data source
    @Bean
    public DataSource dataSource() throws Exception {
        ConfigMap dbConfig = new ConfigMap().getConfigMap("mysql");

        final ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        final String jdbcUrl = "jdbc:mysql://"
                               + dbConfig.getString("host")
                               + "/"
                               + dbConfig.getString("db")
                               + "?useConfigs=maxPerformance&connectTimeout=500&socketTimeout=300000";
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(dbConfig.getString("username"));
        dataSource.setPassword(dbConfig.getString("password"));
        dataSource.setMinPoolSize(5);
        dataSource.setMaxPoolSize(30);
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
            setLocations("classpath:db/migration");
            migrate();
        }
    }
}