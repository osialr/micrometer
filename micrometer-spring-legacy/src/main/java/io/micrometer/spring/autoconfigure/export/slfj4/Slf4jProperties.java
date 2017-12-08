package io.micrometer.spring.autoconfigure.export.slfj4;

import com.codahale.metrics.Slf4jReporter;
import io.micrometer.spring.autoconfigure.export.StepRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@link ConfigurationProperties} for configuring Slf4j metrics export.
 */
@ConfigurationProperties(prefix = "spring.metrics.export.slf4j")
public class Slf4jProperties extends StepRegistryProperties {
    private String logger;
    
    private Slf4jReporter.LoggingLevel level;
    
    private String prefixWith;

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public Slf4jReporter.LoggingLevel getLevel() {
        return level;
    }

    public void setLevel(String level) {
        try {
            this.level = Slf4jReporter.LoggingLevel.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(level + " is not a valid LoggingLevel");
        }
    }

    public String getPrefixWith() {
        return prefixWith;
    }

    public void setPrefixWith(String prefixWith) {
        this.prefixWith = prefixWith;
    }
}
