package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;
import java.io.IOException;

public class FleetManager {
    public static void main(String[] args) {
        DataHub hub = new DataHub();
        // Øker pool-størrelsen til 4 siden vi har 4 enheter totalt
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Starting Multi-Sensor Simulator ===");
        System.out.println("Skriv 'exit' for å avslutte simuleringen manuelt.");

        // Droner
        executor.execute(new DroneSensor("DRONE-Alfa", hub));
        executor.execute(new DroneSensor("DRONE-Bravo", hub));
        executor.execute(new DroneSensor("DRONE-Charlie", hub));
        
        // Værstasjon
        executor.execute(new WeatherSensor("STATION-Oslo", hub));

        // Command Loop: Håndterer både dashboard-oppdatering og 'exit'-sjekk
        try {
            while (true) {
                hub.renderDashboard();

                // Sjekker om det er tastetrykk i bufferen uten å blokkere tråden
                if (System.in.available() > 0) {
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("exit")) {
                        System.out.println("Avslutter simulering...");
                        break;
                    }
                }
                
                // Dashboardet oppdateres hvert sekund for å unngå flimring
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Feil i kontroll-loopen: " + e.getMessage());
        }

        executor.shutdownNow();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        hub.showSummary();
        scanner.close();
    }
}