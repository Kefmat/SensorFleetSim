package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FleetManager {
    public static void main(String[] args) {
        DataHub hub = new DataHub();
        // Øker pool-størrelsen til 4 siden vi har 4 enheter totalt
        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("=== Starting Multi-Sensor Simulator ===");

        // Droner
        executor.execute(new DroneSensor("DRONE-Alfa", hub));
        executor.execute(new DroneSensor("DRONE-Bravo", hub));
        executor.execute(new DroneSensor("DRONE-Charlie", hub));
        
        // Værstasjon
        executor.execute(new WeatherSensor("STATION-Oslo", hub));

        try {
            // Kjører simuleringen i 15 sekunder
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdownNow();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        hub.showSummary();
    }
}