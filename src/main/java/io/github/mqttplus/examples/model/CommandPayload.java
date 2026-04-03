package io.github.mqttplus.examples.model;

import java.util.Map;

public class CommandPayload {

    private String action;
    private Map<String, Object> params;

    public CommandPayload() {
    }

    public CommandPayload(String action, Map<String, Object> params) {
        this.action = action;
        this.params = params;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "CommandPayload{"
                + "action='" + action + '\''
                + ", params=" + params
                + '}';
    }
}
