/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.slf4j;

import com.codahale.metrics.Slf4jReporter;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Create registry that writes metrics to a #{{@link Logger}}
 */
public class Slf4jMeterRegistry extends DropwizardMeterRegistry implements AutoCloseable {
    private final Slf4jReporter reporter;
    private final Logger logger = LoggerFactory.getLogger(Slf4jMeterRegistry.class);

    public Slf4jMeterRegistry(Slf4jConfig config) {
        this(config, HierarchicalNameMapper.DEFAULT, Clock.SYSTEM);
    }

    public Slf4jMeterRegistry(Slf4jConfig config, HierarchicalNameMapper nameMapper, Clock clock) {
        super(nameMapper, clock);
        this.reporter = Slf4jReporter.forRegistry(getDropwizardRegistry())
            .outputTo(LoggerFactory.getLogger(config.logger()))
            .prefixedWith(config.prefixedWith())
            .withLoggingLevel(config.level())
            .build();
        
        this.reporter.start(config.step().toNanos(), TimeUnit.NANOSECONDS);
        
        if (logger.isDebugEnabled()) {
            logger.debug("Will report metrics to logger '{}' at level {} with prefix '{}' at report rate {}",
                        config.logger(),
                        config.level(),
                        config.prefixedWith(),
                        config.step());
        }
    }

    public void stop() {
        logger.debug("Stopping SLF4J Meter Registry");
        this.reporter.stop();
    }

    @Override
    public void close() {
        stop();
    }
}
