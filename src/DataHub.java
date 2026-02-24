package src;

import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHub {
    private final CopyOnWriteArrayList<String> dataLog = new CopyOnWriteArrayList<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public void receiveData(String report) {
        String entry = String.format("[%s] %s", dtf.format(LocalDateTime.now()), report);
        dataLog.add(entry);
        System.out.println("HUB MOTTATT -> " + entry);
    }

    public void showSummary() {
        System.out.println("\n--- SLUTTRAPPORT ---");
        dataLog.forEach(System.out::println);
    }
}