package io.github.mqttplus.examples.listener;

import io.github.mqttplus.core.annotation.MqttListener;
import io.github.mqttplus.core.annotation.MqttTopic;
import io.github.mqttplus.core.model.MqttHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CrossBrokerListener {

    private static final Logger log = LoggerFactory.getLogger(CrossBrokerListener.class);

    @MqttListener(broker = "*", topics = "drone/+/status", payloadType = String.class)
    public void onAnyBrokerStatus(String payload, MqttHeaders headers, @MqttTopic String topic) {
        log.info("[CROSS-BROKER] topic={}, payload={}", topic, payload);
    }
}
