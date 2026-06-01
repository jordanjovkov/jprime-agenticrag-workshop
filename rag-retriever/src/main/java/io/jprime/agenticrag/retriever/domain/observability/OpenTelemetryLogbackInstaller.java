package io.jprime.agenticrag.retriever.domain.observability;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Wires the Logback OpenTelemetryAppender to the Spring-managed OpenTelemetry SDK instance.
 *
 * Logback initializes before the Spring ApplicationContext is ready, so the appender
 * starts in a "buffering" mode. Once this bean initializes, the SDK is installed and
 * the appender starts forwarding log records to the OTLP log exporter.
 */
@Component
public class OpenTelemetryLogbackInstaller implements InitializingBean {

    private final OpenTelemetry openTelemetry;

    public OpenTelemetryLogbackInstaller(OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    @Override
    public void afterPropertiesSet() {
        OpenTelemetryAppender.install(openTelemetry);
    }
}