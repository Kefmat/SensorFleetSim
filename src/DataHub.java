package src;

import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class DataHub {
    private final CopyOnWriteArrayList<String> dataLog = new CopyOnWriteArrayList<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final String LOG_FILE = "telemetry_log.txt";

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public void receiveData(String report) {
        String timestamp = dtf.format(LocalDateTime.now());
        String entry = String.format("[%s] %s", timestamp, report);

        dataLog.add(entry);
        System.out.println(CYAN + "LOG -> " + entry + RESET);

        saveToFile(entry);

        processAlerts(report);
    }

    private void saveToFile(String entry) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter out = new PrintWriter(fw)) {
            out.println(entry);
        } catch (IOException e) {
            System.err.println("Kunne ikke skrive til loggfil: " + e.getMessage());
        }
    }

private void processAlerts(String report) {
        try {
            String droneId = report.split(" \\| ")[0];
            String batPart = report.split("Bat: ")[1].split("%")[0].replace(",", ".");
            double battery = Double.parseDouble(batPart);

            if (battery < 20.0) {
                String msg = "  WARNING [" + droneId + "]: Low Battery! (" + battery + "%)";
                System.err.println(YELLOW + msg + RESET);
                saveToFile("ALERT: " + msg); 
            }

            if (report.contains("Alt:")) {
                String altPart = report.split("Alt: ")[1].split("m")[0].replace(",", ".");
                double altitude = Double.parseDouble(altPart);
                if (altitude < 50.0) {
                    String msg = " CRITICAL [" + droneId + "]: Unsafe Altitude! (" + altitude + "m)";
                    System.err.println(RED + msg + RESET);
                    saveToFile("ALERT: " + msg);
                }
            }

            if (report.contains("Wind:")) {
                String windPart = report.split("Wind: ")[1].split("m/s")[0].replace(",", ".");
                double wind = Double.parseDouble(windPart);
                if (wind > 15.0) {
                    String msg = " STORM WARNING [" + droneId + "]: High Winds! (" + wind + "m/s)";
                    System.err.println(RED + msg + RESET);
                    saveToFile("ALERT: " + msg);
                }
            }
        } catch (Exception e) {

        }
    }

    public void showSummary() {
        System.out.println("\n" + CYAN + "=== SESSION SUMMARY ===" + RESET);
        for (String log : dataLog) {
            System.out.println(log);
        }
        System.out.println("Full log saved to: " + LOG_FILE);
    }
}