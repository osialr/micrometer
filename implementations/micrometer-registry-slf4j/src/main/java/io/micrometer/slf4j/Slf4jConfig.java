package io.micrometer.slf4j;

import com.codahale.metrics.Slf4jReporter;
import io.micrometer.core.instrument.step.StepRegistryConfig;

/**
 * Configuration for Slf4j Registry
 */
public interface Slf4jConfig extends StepRegistryConfig {
    
    Slf4jConfig DEFAULT = k -> null;

    @Override
    default String prefix() {
        return "slf4j";
    }
    
    default String logger() {
        String v = get(prefix() + ".logger");
        return v == null ? "metrics" : v;
    }
    
    default Slf4jReporter.LoggingLevel level() {
        String v = get(prefix() + ".level");
        if (v != null) {
            return Slf4jReporter.LoggingLevel.valueOf(v.toUpperCase());
        } else {
            return Slf4jReporter.LoggingLevel.INFO;
        }
    }
    
    default String prefixedWith() {
        String v = get(prefix() + ".prefixedWith");
        return v == null ? "" : v;
    }
    
}
