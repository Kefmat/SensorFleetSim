package src;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class DataHub {
    private final CopyOnWriteArrayList<String> dataLog = new CopyOnWriteArrayList<>();
    private final ConcurrentHashMap<String, String> latestReports = new ConcurrentHashMap<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final String LOG_FILE = "telemetry_log.txt";

    // Statistikk-variabler
    private int totalReports = 0;
    private int batteryAlerts = 0;
    private int altitudeAlerts = 0;
    private int stormWarnings = 0;
    private double maxWindSpeed = 0;
    private double minBatteryObserved = 100.0;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public void receiveData(String report) {
        totalReports++;
        String timestamp = dtf.format(LocalDateTime.now());
        String entry = String.format("[%s] %s", timestamp, report);
        
        dataLog.add(entry);
        saveToFile(entry);

        String sensorId = report.split(" \\| ")[0];
        latestReports.put(sensorId, report);

        // Dashboardet kalles nå eksternt for å unngå gjentakelser per mottatt rapport
        processAlerts(report);
    }

    public void renderDashboard() {
        // Tømmer skjermen
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(CYAN + "=== SENSOR FLEET DASHBOARD ===" + RESET);
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-15s | %-10s | %-25s\n", "ID", "BATTERY", "TELEMETRY");
        System.out.println("------------------------------------------------------------");

        latestReports.forEach((id, data) -> {
            try {
                String battery = data.split("Bat: ")[1].split("%")[0] + "%";
                String telemetry = data.split(" \\| ", 2)[1].split(" \\| ", 2)[1];
                System.out.printf("%-15s | %-10s | %-25s\n", id, battery, telemetry);
            } catch (Exception e) {
                // Ignorerer formateringsfeil
            }
        });
        System.out.println("------------------------------------------------------------");
        System.out.println("Status: " + totalReports + " rapporter mottatt. Skriv 'exit' for stop.");
        System.out.print("> ");
    }

    private void saveToFile(String entry) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(entry);
        } catch (IOException e) {
            System.err.println("Feil ved skriving til fil: " + e.getMessage());
        }
    }

    private void processAlerts(String report) {
        try {
            String sensorId = report.split(" \\| ")[0];
            
            // Batteri-statistikk
            double battery = Double.parseDouble(report.split("Bat: ")[1].split("%")[0].replace(",", "."));
            if (battery < minBatteryObserved) minBatteryObserved = battery;
            if (battery < 20.0) {
                batteryAlerts++;
                System.err.println(YELLOW + "WARNING [" + sensorId + "]: Low Battery! (" + battery + "%)" + RESET);
            }

            // Høyde-statistikk
            if (report.contains("Alt:")) {
                double altitude = Double.parseDouble(report.split("Alt: ")[1].split("m")[0].replace(",", "."));
                if (altitude < 50.0) {
                    altitudeAlerts++;
                    System.err.println(RED + "CRITICAL [" + sensorId + "]: Unsafe Altitude! (" + altitude + "m)" + RESET);
                }
            }

            // Vind-statistikk
            if (report.contains("Wind:")) {
                double wind = Double.parseDouble(report.split("Wind: ")[1].split("m/s")[0].replace(",", "."));
                if (wind > maxWindSpeed) maxWindSpeed = wind;
                if (wind > 15.0) {
                    stormWarnings++;
                    System.err.println(RED + "STORM WARNING [" + sensorId + "]: High Winds! (" + wind + "m/s)" + RESET);
                }
            }
        } catch (Exception e) {
            // Ignorerer parsing-feil
        }
    }

    public void showSummary() {
        System.out.println("\n" + CYAN + "=== FINAL SESSION STATISTICS ===" + RESET);
        System.out.println("Totalt antall rapporter behandlet: " + totalReports);
        System.out.println("Laveste observerte batterinivå: " + String.format("%.1f%%", minBatteryObserved));
        System.out.println("Høyeste observerte vindstyrke: " + String.format("%.1f m/s", maxWindSpeed));
        System.out.println("-----------------------------------------");
        System.out.println(YELLOW + "Antall Batteri-advarsler: " + batteryAlerts + RESET);
        System.out.println(RED + "Antall Høyde-kritiske feil: " + altitudeAlerts + RESET);
        System.out.println(RED + "Antall Storm-varsler: " + stormWarnings + RESET);
        System.out.println("-----------------------------------------");
        System.out.println("Full logg er lagret i: " + LOG_FILE);
    }
}