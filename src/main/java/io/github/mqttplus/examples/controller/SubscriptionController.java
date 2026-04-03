package io.github.mqttplus.examples.controller;

import io.github.mqttplus.spring.event.MqttSubscriptionRefreshEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionController.class);

    private final ApplicationEventPublisher publisher;

    public SubscriptionController(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/subscribe")
    public String subscribe(@RequestParam String droneSn) {
        String topic = "drone/" + droneSn + "/status";
        publisher.publishEvent(new MqttSubscriptionRefreshEvent(
                MqttSubscriptionRefreshEvent.Action.SUBSCRIBE, "cloud", topic, 1));
        log.info("[DYNAMIC-SUB] subscribed to {}", topic);
        return "Subscribed to " + topic;
    }

    @PostMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String droneSn) {
        String topic = "drone/" + droneSn + "/status";
        publisher.publishEvent(new MqttSubscriptionRefreshEvent(
                MqttSubscriptionRefreshEvent.Action.UNSUBSCRIBE, "cloud", topic, 0));
        log.info("[DYNAMIC-SUB] unsubscribed from {}", topic);
        return "Unsubscribed from " + topic;
    }
}
