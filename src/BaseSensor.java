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
        while (running && batteryLevel > 0) {
            collectData();
            hub.receiveData(getStatusReport());
            batteryLevel -= (random.nextDouble() * 2.0);
            try { Thread.sleep(2000); } catch (InterruptedException e) { break; }
        }
    }
    @Override
    public String getSensorId() { return id; }
}