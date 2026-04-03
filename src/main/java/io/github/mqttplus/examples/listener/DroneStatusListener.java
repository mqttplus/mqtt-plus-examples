package io.github.mqttplus.examples.listener;

import io.github.mqttplus.core.annotation.MqttListener;
import io.github.mqttplus.core.annotation.MqttTopic;
import io.github.mqttplus.core.model.MqttHeaders;
import io.github.mqttplus.examples.model.DroneStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DroneStatusListener {

    private static final Logger log = LoggerFactory.getLogger(DroneStatusListener.class);

    @MqttListener(broker = "cloud", topics = "drone/+/status", payloadType = DroneStatus.class)
    public void onStatus(DroneStatus status, MqttHeaders headers, @MqttTopic String topic) {
        log.info("[STATUS] topic={}, drone={}, battery={}%, pos=({},{},{}), mode={}",
                topic,
                status.getSn(),
                status.getBattery(),
                status.getLatitude(),
                status.getLongitude(),
                status.getAltitude(),
                status.getFlightMode());
    }
}
