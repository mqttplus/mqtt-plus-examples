package io.github.mqttplus.examples.model;

public class DroneStatus {

    private String sn;
    private double latitude;
    private double longitude;
    private double altitude;
    private int battery;
    private String flightMode;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getFlightMode() {
        return flightMode;
    }

    public void setFlightMode(String flightMode) {
        this.flightMode = flightMode;
    }

    @Override
    public String toString() {
        return "DroneStatus{"
                + "sn='" + sn + '\''
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", altitude=" + altitude
                + ", battery=" + battery
                + ", flightMode='" + flightMode + '\''
                + '}';
    }
}
