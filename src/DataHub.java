package src;

import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHub {
    private final CopyOnWriteArrayList<String> dataLog = new CopyOnWriteArrayList<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public void receiveData(String report) {
        String timestamp = dtf.format(LocalDateTime.now());
        String entry = String.format("[%s] %s", timestamp, report);
        dataLog.add(entry);

        System.out.println(CYAN + "LOG -> " + entry + RESET);
        processAlerts(report);
    }

    private void processAlerts(String report) {
        try {
            String droneId = report.split(" \\| ")[0];
            
            // Felles parsing for batteri (alle sensorer har batteri)
            String batPart = report.split("Bat: ")[1].split("%")[0].replace(",", ".");
            double battery = Double.parseDouble(batPart);

            if (battery < 20.0) {
                System.err.println(YELLOW + "  WARNING [" + droneId + "]: Low Battery! (" + battery + "%)" + RESET);
            }

            // Spesifikk logikk for Drone (Høyde)
            if (report.contains("Alt:")) {
                String altPart = report.split("Alt: ")[1].split("m")[0].replace(",", ".");
                double altitude = Double.parseDouble(altPart);
                if (altitude < 50.0) {
                    System.err.println(RED + " CRITICAL [" + droneId + "]: Unsafe Altitude! (" + altitude + "m)" + RESET);
                }
            }

            // Spesifikk logikk for Værstasjon (Vind)
            if (report.contains("Wind:")) {
                String windPart = report.split("Wind: ")[1].split("m/s")[0].replace(",", ".");
                double wind = Double.parseDouble(windPart);
                if (wind > 15.0) {
                    System.err.println(RED + " STORM WARNING [" + droneId + "]: High Winds! (" + wind + "m/s)" + RESET);
                }
            }

        } catch (Exception e) {
            // Ignorerer parsing-feil for å holde huben kjørende
        }
    }

    public void showSummary() {
        System.out.println("\n" + CYAN + "=== SESSION SUMMARY ===" + RESET);
        for (String log : dataLog) {
            System.out.println(log);
        }
    }
}