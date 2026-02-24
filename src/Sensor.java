package src;

public interface Sensor {
    void collectData();
    String getStatusReport();
    String getSensorId();
}