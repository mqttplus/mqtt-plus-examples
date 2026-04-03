package io.github.mqttplus.examples.publisher;

import io.github.mqttplus.core.MqttTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class StreamNotificationPublisher {

    private static final Logger log = LoggerFactory.getLogger(StreamNotificationPublisher.class);

    private final MqttTemplate mqttTemplate;

    public StreamNotificationPublisher(MqttTemplate mqttTemplate) {
        this.mqttTemplate = mqttTemplate;
    }

    public CompletableFuture<Void> notifyStreamStarted(String droneSn, String streamUrl) {
        String payload = "{\"sn\":\"" + droneSn + "\",\"url\":\"" + streamUrl + "\"}";
        return mqttTemplate.publishAsync("cloud", "drone/" + droneSn + "/stream", payload)
                .thenRun(() -> log.info("[STREAM] notification sent for drone/{}", droneSn));
    }
}
