package io.github.mqttplus.examples.publisher;

import io.github.mqttplus.core.MqttTemplate;
import io.github.mqttplus.examples.model.CommandPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CommandPublisher {

    private static final Logger log = LoggerFactory.getLogger(CommandPublisher.class);

    private final MqttTemplate mqttTemplate;

    public CommandPublisher(MqttTemplate mqttTemplate) {
        this.mqttTemplate = mqttTemplate;
    }

    public void sendCommand(String droneSn, CommandPayload command) {
        mqttTemplate.publish("cloud", "drone/" + droneSn + "/command", command, 1, false);
        log.info("[COMMAND] sent to drone/{}/command: {}", droneSn, command.getAction());
    }
}
