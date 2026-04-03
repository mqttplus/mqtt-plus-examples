package io.github.mqttplus.examples.model;

public class AlertEvent {

    private String level;
    private String source;
    private String message;

    public AlertEvent() {
    }

    public AlertEvent(String level, String source, String message) {
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AlertEvent{"
                + "level='" + level + '\''
                + ", source='" + source + '\''
                + ", message='" + message + '\''
                + '}';
    }
}
