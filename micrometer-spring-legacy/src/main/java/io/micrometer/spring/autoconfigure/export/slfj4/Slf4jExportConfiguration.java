package io.micrometer.spring.autoconfigure.export.slfj4;

import com.codahale.metrics.Slf4jReporter;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import io.micrometer.slf4j.Slf4jConfig;
import io.micrometer.slf4j.Slf4jMeterRegistry;
import io.micrometer.spring.autoconfigure.export.DefaultStepRegistryConfig;
import io.micrometer.spring.autoconfigure.export.MetricsExporter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration for exporting metrics to slf4j
 */
@Configuration
@ConditionalOnClass(Slf4jMeterRegistry.class)
@Import(Slf4jProperties.class)
public class Slf4jExportConfiguration {

    private class DefaultSlf4jRegistryConfig extends DefaultStepRegistryConfig implements Slf4jConfig {
        private final Slf4jProperties props;

        DefaultSlf4jRegistryConfig(Slf4jProperties props) {
            super(props);
            this.props = props;
        }

        @Override
        public String logger() {
            return props.getLogger() == null ? DEFAULT.logger() : props.getLogger();
        }

        @Override
        public Slf4jReporter.LoggingLevel level() {
            return props.getLevel() == null ? DEFAULT.level() : props.getLevel();
        }

        @Override
        public String prefixedWith() {
            return props.getPrefixWith() == null ? DEFAULT.prefixedWith() : props.getPrefixWith();
        }
    }


    @Bean
    @ConditionalOnProperty(value = "spring.metrics.export.slf4j.enabled", matchIfMissing = true)
    public MetricsExporter slf4jExporter(Slf4jConfig config, HierarchicalNameMapper nameMapper, Clock clock) {
        return () -> new Slf4jMeterRegistry(config, nameMapper, clock);
    }

    @Bean
    @ConditionalOnMissingBean(Slf4jConfig.class)
    public Slf4jConfig slf4jConfig(Slf4jProperties props) {
        return new DefaultSlf4jRegistryConfig(props);
    }

    @Bean
    @ConditionalOnMissingBean
    public Clock micrometerClock() {
        return Clock.SYSTEM;
    }

    @Bean
    @ConditionalOnMissingBean
    public HierarchicalNameMapper hierarchicalNameMapper() {
        return HierarchicalNameMapper.DEFAULT;
    }
}
