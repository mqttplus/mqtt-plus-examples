package io.github.mqttplus.examples.publisher;

import io.github.mqttplus.core.MqttTemplate;
import io.github.mqttplus.examples.model.AlertEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AlertPublisher {

    private static final Logger log = LoggerFactory.getLogger(AlertPublisher.class);

    private final MqttTemplate mqttTemplate;

    public AlertPublisher(MqttTemplate mqttTemplate) {
        this.mqttTemplate = mqttTemplate;
    }

    public void publishAlert(String level, String source, String message) {
        AlertEvent alert = new AlertEvent(level, source, message);
        mqttTemplate.publish("cloud", "alert/" + level + "/" + source, alert, 1, true);
        log.info("[ALERT-PUB] published [retained] alert/{}/{}: {}", level, source, message);
    }
}
