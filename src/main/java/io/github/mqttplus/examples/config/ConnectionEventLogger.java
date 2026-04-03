package io.github.mqttplus.examples.config;

import io.github.mqttplus.core.adapter.MqttConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConnectionEventLogger implements MqttConnectionListener {

    private static final Logger log = LoggerFactory.getLogger(ConnectionEventLogger.class);

    @Override
    public void onConnected(String brokerId) {
        log.info("[CONNECTION] connected to broker [{}]", brokerId);
    }

    @Override
    public void onConnectionLost(String brokerId, Throwable cause) {
        String message = cause == null ? "unknown" : cause.getMessage();
        log.warn("[CONNECTION] lost connection to broker [{}]: {}", brokerId, message);
    }

    @Override
    public void onDisconnected(String brokerId) {
        log.info("[CONNECTION] disconnected from broker [{}]", brokerId);
    }
}
