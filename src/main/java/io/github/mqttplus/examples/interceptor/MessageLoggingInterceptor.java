package io.github.mqttplus.examples.interceptor;

import io.github.mqttplus.core.interceptor.MqttMessageInterceptor;
import io.github.mqttplus.core.model.MqttContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessageLoggingInterceptor implements MqttMessageInterceptor {

    private static final Logger log = LoggerFactory.getLogger(MessageLoggingInterceptor.class);

    @Override
    public void beforeHandle(MqttContext context) {
        log.debug("[INTERCEPTOR] >> broker={}, topic={}, payload={} bytes",
                context.getBrokerId(),
                context.getTopic(),
                context.getPayload().length);
    }

    @Override
    public void afterHandle(MqttContext context) {
        log.debug("[INTERCEPTOR] << broker={}, topic={}", context.getBrokerId(), context.getTopic());
    }
}
