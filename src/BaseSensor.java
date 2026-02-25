package src;
import java.util.Random;

public abstract class BaseSensor implements Sensor, Runnable {
    protected String id;
    protected DataHub hub;
    protected double batteryLevel = 100.0;
    protected Random random = new Random();
    protected boolean running = true;

    public BaseSensor(String id, DataHub hub) {
        this.id = id;
        this.hub = hub;
    }

    @Override
    public void run() {
        // Simulerer til batteriet er helt tomt
        while (running && batteryLevel > 0) {
            collectData();
            hub.receiveData(getStatusReport());

            // Tapper batteriet raskere for å teste Alert-systemet (mellom 2% og 7% per hopp)
            batteryLevel -= (2.0 + random.nextDouble() * 5.0);
            
            // Sørger for at batteriet ikke blir negativt
            if (batteryLevel < 0) batteryLevel = 0;

            try { 
                Thread.sleep(1500);
            } catch (InterruptedException e) { 
                break; 
            }
        }
        System.out.println("!!! SENSOR " + id + " SHUTTING DOWN: Battery Depleted !!!");
    }

    @Override
    public String getSensorId() { return id; }
}