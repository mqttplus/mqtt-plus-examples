package io.github.mqttplus.examples.listener;

import io.github.mqttplus.core.annotation.MqttListener;
import io.github.mqttplus.core.annotation.MqttTopic;
import io.github.mqttplus.core.model.MqttHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DroneHeartbeatListener {

    private static final Logger log = LoggerFactory.getLogger(DroneHeartbeatListener.class);

    @MqttListener(broker = "cloud", topics = "drone/+/heartbeat", qos = 0, payloadType = String.class)
    public void onHeartbeat(String payload, MqttHeaders headers, @MqttTopic String topic) {
        Object qos = headers.get("qos");
        log.info("[HEARTBEAT] topic={}, qos={}, payload={}", topic, qos, payload);
    }
}
