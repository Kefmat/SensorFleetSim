package src;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ReportGenerator {
    
    public static void generateMarkdownReport(
        int totalReports, 
        int batAlerts, 
        int altAlerts, 
        int stormAlerts, 
        double minBat, 
        double maxWind
    ) {
        // Definerer filnavn, kan overstyres via System Properties
        String fileName = System.getProperty("report.file", "session_report.md");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        try (FileWriter fw = new FileWriter(fileName);
             PrintWriter out = new PrintWriter(fw)) {
            
            // Rapport-header
            out.println("# Engineering Session Report");
            out.println("### Generation Timestamp: " + dtf.format(LocalDateTime.now()));
            out.println("\n---");
            
            // 1. System Metadata (Viktig for sporbarhet i DevOps)
            out.println("\n## 1. System Metadata");
            out.println("- **Java Version:** " + System.getProperty("java.version"));
            out.println("- **Operating System:** " + System.getProperty("os.name"));
            out.println("- **Session ID:** " + System.currentTimeMillis());

            // 2. Visualisering ved bruk av Mermaid kakediagram
            out.println("\n## 2. Fleet Status Visualization");
            out.println("The following chart visualizes the distribution of incidents during this session:");
            out.println("\n```mermaid");
            out.println("pie title Incident Distribution");
            out.println("    \"Battery Warnings\" : " + batAlerts);
            out.println("    \"Altitude Criticals\" : " + altAlerts);
            out.println("    \"Storm Warnings\" : " + stormAlerts);
            
            // Fallback for å sikre at diagrammet tegnes selv uten feil
            if (batAlerts + altAlerts + stormAlerts == 0) {
                out.println("    \"Normal Operation\" : 1");
            }
            out.println("```");

            // 3. Tekniske data i tabellform
            out.println("\n## 3. Technical Statistics");
            out.println("| Metric | Value |");
            out.println("| :--- | :--- |");
            out.printf("| Total Packets Processed | %d |\n", totalReports);
            out.printf("| Minimum Battery Observed | %.1f%% |\n", minBat);
            out.printf("| Peak Wind Velocity | %.1f m/s |\n", maxWind);
            
            // 4. Oppsummering av systemintegritet
            out.println("\n## 4. Safety Performance Analysis");
            out.println("- Battery Health: " + (batAlerts > 0 ? "Degraded" : "Optimal"));
            out.println("- Flight Safety: " + (altAlerts > 0 ? "Violations Logged" : "Nominal"));
            out.println("- Environmental Conditions: " + (stormAlerts > 0 ? "Adverse" : "Stable"));
            
            out.println("\n\n---");
            out.println("*Automated Report Service - SensorFleetSim Core*");
            
            System.out.println("Engineering report generated: " + fileName);
            
        } catch (IOException e) {
            
            System.err.println("Critical Error: Report generation failed: " + e.getMessage());
        }
    }
}