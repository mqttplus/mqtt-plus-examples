package io.github.mqttplus.examples.listener;

import io.github.mqttplus.core.annotation.MqttListener;
import io.github.mqttplus.core.annotation.MqttTopic;
import io.github.mqttplus.examples.model.AlertEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlertListener {

    private static final Logger log = LoggerFactory.getLogger(AlertListener.class);

    @MqttListener(broker = "cloud", topics = "alert/#", qos = 1, payloadType = AlertEvent.class)
    public void onAlert(AlertEvent alert, @MqttTopic String topic) {
        log.warn("[ALERT] topic={}, level={}, source={}, message={}",
                topic,
                alert.getLevel(),
                alert.getSource(),
                alert.getMessage());
    }
}
