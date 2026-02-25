package src;

import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHub {
    private final CopyOnWriteArrayList<String> dataLog = new CopyOnWriteArrayList<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    // ANSI fargekoder for terminal-output
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
            
            // Henter ut batteri
            String batPart = report.split("Bat: ")[1].split("%")[0].replace(",", ".");
            double battery = Double.parseDouble(batPart);

            // Henter ut høyde
            String altPart = report.split("Alt: ")[1].split("m")[0].replace(",", ".");
            double altitude = Double.parseDouble(altPart);

            // Henter ut ID (første del av strengen)
            String droneId = report.split(" \\| ")[0];

            // Sjekk terskelverdier
            if (battery < 20.0) {
                System.err.println(YELLOW + "  WARNING [" + droneId + "]: Low Battery! (" + battery + "%)" + RESET);
            }

            if (altitude < 50.0) {
                System.err.println(RED + " CRITICAL [" + droneId + "]: Unsafe Altitude! (" + altitude + "m)" + RESET);
            }

        } catch (Exception e) {
            System.err.println(RED + "ERROR: Failed to process report - " + e.getMessage() + RESET);
        }
    }

    public void showSummary() {
        System.out.println("\n" + CYAN + "=== SESSION SUMMARY ===" + RESET);
        for (String log : dataLog) {
            System.out.println(log);
        }
    }
}