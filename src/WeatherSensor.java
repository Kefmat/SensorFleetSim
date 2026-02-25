package src;

public class WeatherSensor extends BaseSensor {
    private double temperature;
    private double windSpeed;

    public WeatherSensor(String id, DataHub hub) {
        super(id, hub);
        this.temperature = 15.0;
        this.windSpeed = 5.0;    
    }

    @Override
    public void collectData() {
        // Simulerer små endringer i været
        temperature += (random.nextDouble() - 0.5) * 0.5;
        windSpeed += (random.nextDouble() - 0.5) * 2.0;
        if (windSpeed < 0) windSpeed = 0;
    }

    @Override
    public String getStatusReport() {
        
        return String.format("WEATHER-%s | Bat: %.1f%% | Temp: %.1fC | Wind: %.1fm/s", 
                              id, batteryLevel, temperature, windSpeed);
    }
}