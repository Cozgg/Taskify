package com.ccq.configs;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private static final Properties DOTENV = loadDotenv();

    public static String get(String key) {
        String value = System.getenv(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        value = DOTENV.getProperty(key);
        if (value != null && !value.isBlank()) {
            return value;
        }
        return null;
    }

    private static Properties loadDotenv() {
        Properties props = new Properties();
        Path path = findDotenv();
        if (path == null) {
            return props;
        }

        try (FileInputStream input = new FileInputStream(path.toFile())) {
            props.load(input);
        } catch (IOException ignored) {
        }
        return props;
    }

    private static Path findDotenv() {
        String configuredPath = System.getProperty("taskify.env.file");
        Path dotenv = findExistingFile(configuredPath);
        if (dotenv != null) {
            return dotenv;
        }

        configuredPath = System.getenv("TASKIFY_ENV_FILE");
        dotenv = findExistingFile(configuredPath);
        if (dotenv != null) {
            return dotenv;
        }

        dotenv = findDotenvFrom(Path.of(System.getProperty("user.dir")).toAbsolutePath());
        if (dotenv != null) {
            return dotenv;
        }

        try {
            Path classpath = Path.of(EnvConfig.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .toAbsolutePath();
            return findDotenvFrom(classpath);
        } catch (URISyntaxException | SecurityException ex) {
            return null;
        }
    }

    private static Path findExistingFile(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        Path path = Path.of(value).toAbsolutePath();
        if (Files.isRegularFile(path)) {
            return path;
        }
        return null;
    }

    private static Path findDotenvFrom(Path start) {
        Path current = Files.isRegularFile(start) ? start.getParent() : start;
        while (current != null) {
            Path dotenv = current.resolve(".env");
            if (Files.isRegularFile(dotenv)) {
                return dotenv;
            }
            current = current.getParent();
        }
        return null;
    }
}
