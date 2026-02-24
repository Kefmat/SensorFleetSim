package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FleetManager {
    public static void main(String[] args) {
        DataHub hub = new DataHub();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("=== Starter Simulator ===\n");

        executor.execute(new DroneSensor("Alfa", hub, 59.91, 10.75));
        executor.execute(new DroneSensor("Bravo", hub, 59.92, 10.76));
        executor.execute(new DroneSensor("Charlie", hub, 59.93, 10.77));

        try { Thread.sleep(10000); } catch (InterruptedException e) { }

        executor.shutdownNow();
        hub.showSummary();
    }
}