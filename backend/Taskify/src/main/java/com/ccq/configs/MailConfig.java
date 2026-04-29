package com.ccq.configs;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:configs.properties")
public class MailConfig {

    @Autowired
    private Environment env;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(getProperty("mail.host", "smtp.gmail.com"));
        sender.setPort(Integer.parseInt(getProperty("mail.port", "587")));
        sender.setUsername(getProperty("mail.username", ""));
        sender.setPassword(getProperty("mail.password", ""));

        Properties props = sender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.connectiontimeout", getProperty("mail.smtp.connectiontimeout", "5000"));
        props.put("mail.smtp.timeout", getProperty("mail.smtp.timeout", "5000"));
        props.put("mail.smtp.writetimeout", getProperty("mail.smtp.writetimeout", "5000"));
        props.put("mail.debug", getProperty("mail.debug", "false"));
        return sender;
    }

    private String getProperty(String key, String defaultValue) {
        String envKey = key.toUpperCase().replace('.', '_').replace('-', '_');
        String value = System.getenv(envKey);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return env.getProperty(key, defaultValue);
    }
}
