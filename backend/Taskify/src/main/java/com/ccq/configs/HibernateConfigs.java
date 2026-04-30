/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ccq.configs;

import java.util.Properties;

import javax.sql.DataSource;

import static org.hibernate.cfg.JdbcSettings.DIALECT;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
/**
 *
 * @author Admin
 */
@Configuration
@PropertySource("classpath:database.properties")
@ComponentScan(basePackages = {"com.ccq.configs", "com.ccq.utils"})
public class HibernateConfigs {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setPackagesToScan(new String[]{"com.ccq.pojo"});
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(getProperty("hibernate.connection.driverClass", "com.mysql.cj.jdbc.Driver"));
        dataSource.setUrl(getProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/taskifydb"));
        dataSource.setUsername(getProperty("hibernate.connection.username", ""));
        dataSource.setPassword(getSecret("DB_PASSWORD", ""));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put(DIALECT, getProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect"));
        props.put(SHOW_SQL, getProperty("hibernate.showSql", "true"));
        props.put("hibernate.hbm2ddl.auto", getProperty("hibernate.hbm2ddl.auto", "update"));
        return props;
    }

    private String getProperty(String key, String defaultValue) {
        try {
            String value = env.getProperty(key);
            if (value != null && !value.isBlank() && !value.contains("${")) {
                return value;
            }
        } catch (IllegalArgumentException ex) {
        }
        return defaultValue;
    }

    private String getSecret(String key, String defaultValue) {
        String value = EnvConfig.get(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return defaultValue;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(getSessionFactory().getObject());
        return transactionManager;
    }
}
