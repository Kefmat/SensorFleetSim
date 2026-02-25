package src;

public class DroneSensor extends BaseSensor {
    private double lat = 59.91;
    private double lon = 10.75;
    private double alt = 100.0;

    public DroneSensor(String id, DataHub hub) {
        super(id, hub); // Sender ID og Hub videre til BaseSensor
    }

    @Override
    public void collectData() {
        // Simulerer litt bevegelse
        lat += (random.nextDouble() - 0.5) * 0.001;
        lon += (random.nextDouble() - 0.5) * 0.001;
        alt += (random.nextDouble() - 0.5) * 5.0;
    }

    @Override
    public String getStatusReport() {
        return String.format("%s | Bat: %.1f%% | Alt: %.1fm", 
                              id, batteryLevel, alt);
    }
}