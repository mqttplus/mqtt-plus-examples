package io.github.mqttplus.examples.controller;

import io.github.mqttplus.examples.model.CommandPayload;
import io.github.mqttplus.examples.publisher.AlertPublisher;
import io.github.mqttplus.examples.publisher.CommandPublisher;
import io.github.mqttplus.examples.publisher.StreamNotificationPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final CommandPublisher commandPublisher;
    private final AlertPublisher alertPublisher;
    private final StreamNotificationPublisher streamPublisher;

    public DemoController(CommandPublisher commandPublisher,
                          AlertPublisher alertPublisher,
                          StreamNotificationPublisher streamPublisher) {
        this.commandPublisher = commandPublisher;
        this.alertPublisher = alertPublisher;
        this.streamPublisher = streamPublisher;
    }

    @PostMapping("/command")
    public String sendCommand(@RequestParam String droneSn,
                              @RequestParam(defaultValue = "takeoff") String action) {
        commandPublisher.sendCommand(droneSn, new CommandPayload(action, Map.of()));
        return "Command '" + action + "' sent to " + droneSn;
    }

    @PostMapping("/alert")
    public String publishAlert(@RequestParam(defaultValue = "warning") String level,
                               @RequestParam(defaultValue = "battery") String source,
                               @RequestParam(defaultValue = "Low battery") String message) {
        alertPublisher.publishAlert(level, source, message);
        return "Alert published: " + level + "/" + source;
    }

    @PostMapping("/stream")
    public String notifyStream(@RequestParam String droneSn,
                               @RequestParam(defaultValue = "rtmp://localhost/live") String url) {
        streamPublisher.notifyStreamStarted(droneSn, url);
        return "Stream notification sent for " + droneSn;
    }
}
