# SensorFleetSim - Real-time IoT Fleet Simulator

A robust Java-based simulation of a sensor fleet (drones, IoT devices) using multi-threaded architecture and thread-safe data synchronization.

## System Architecture
The project demonstrates core software engineering principles:
- **Abstraction & Polymorphism:** Using a `Sensor` interface and `BaseSensor` abstract class to allow the system to scale with new sensor types without modifying the core logic.
- **Concurrency (Multithreading):** Each sensor operates on its own thread using Java's `Runnable` interface, simulating independent hardware units.
- **Thread-Safety:** A central `DataHub` utilizes `CopyOnWriteArrayList` and concurrent patterns to handle simultaneous data streams from multiple sensors without data corruption.

## Class Model
- **`Sensor` (Interface):** Defines the contract (methods) for all sensing units.
- **`BaseSensor` (Abstract Class):** Implements the lifecycle logic (battery drain, thread loop, hub communication).
- **`DroneSensor` (Concrete Class):** Simulates a drone with 3D positional telemetry (Lat, Lon, Alt).
- **`DataHub` (Class):** Acts as the central telemetry processor and data logger.
- **`FleetManager` (Main):** Orchestrates the simulation using an `ExecutorService` thread pool.

## Roadmap & Future Enhancements
- **Critical Alert Logic:** Implement a threshold monitoring system (e.g., auto-triggering alerts for low battery or unsafe altitude).
- **Sensor Diversity:** Add `WeatherSensor` and `MaritimeSensor` to demonstrate the Open/Closed Principle.
- **Data Persistence:** Export session logs to JSON or a database for post-flight analysis.
- **Real-time Dashboard:** Develop a CLI-based visual dashboard to track the live status of the entire fleet.