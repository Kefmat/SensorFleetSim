package src;

public class DroneSensor extends BaseSensor {
    private double lat, lon, alt;

    public DroneSensor(String id, DataHub hub, double lat, double lon) {
        super(id, hub);
        this.lat = lat;
        this.lon = lon;
        this.alt = 100.0;
    }

    @Override
    public void collectData() {
        lat += (random.nextDouble() - 0.5) * 0.001;
        lon += (random.nextDouble() - 0.5) * 0.001;
        alt += (random.nextDouble() - 0.5) * 5.0;
    }

    @Override
    public String getStatusReport() {
        return String.format("DRONE-%s | Bat: %.1f%% | Alt: %.1fm", id, batteryLevel, alt);
    }
}