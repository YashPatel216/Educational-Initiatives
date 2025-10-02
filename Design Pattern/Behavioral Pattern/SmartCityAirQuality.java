import java.util.*;

// Subscriber interface (Observer)
interface Subscriber {
    void notifyUpdate(int aqi);
}

// Publisher (Subject)
class AirMonitor {
    private List<Subscriber> subscribers = new ArrayList<>();
    private int currentAqi;

    public void register(Subscriber sub) {
        subscribers.add(sub);
    }

    public void unregister(Subscriber sub) {
        subscribers.remove(sub);
    }

    public void updateAqi(int newAqi) {
        this.currentAqi = newAqi;
        System.out.println("\n=== AQI Update from Sensor: " + newAqi + " ===");
        broadcast();
    }

    private void broadcast() {
        for (Subscriber sub : subscribers) {
            sub.notifyUpdate(currentAqi);
        }
    }
}

// Concrete Subscriber 1 → Air Purifier
class PurifierUnit implements Subscriber {
    private String location;

    public PurifierUnit(String location) {
        this.location = location;
    }

    @Override
    public void notifyUpdate(int aqi) {
        String mode;
        if (aqi <= 100) {
            mode = "OFF";
        } else if (aqi <= 180) {
            mode = "LOW";
        } else if (aqi <= 250) {
            mode = "MEDIUM";
        } else {
            mode = "HIGH";
        }
        System.out.println("[Purifier@" + location + "] AQI=" + aqi + " → Mode set to " + mode);
    }
}

// Concrete Subscriber 2 → Traffic Panel
class TrafficPanel implements Subscriber {
    private String area;

    public TrafficPanel(String area) {
        this.area = area;
    }

    @Override
    public void notifyUpdate(int aqi) {
        if (aqi > 200) {
            System.out.println("[TrafficPanel-" + area + "] AQI=" + aqi + " → Showing WARNING  | Heavy vehicle ban");
        } else {
            System.out.println("[TrafficPanel-" + area + "] AQI=" + aqi + " → Normal traffic flow");
        }
    }
}

// Concrete Subscriber 3 → Citizen Mobile App
class MobileNotifier implements Subscriber {
    private String username;
    private int alertsReceived = 0;

    public MobileNotifier(String username) {
        this.username = username;
    }

    @Override
    public void notifyUpdate(int aqi) {
        if (aqi > 120) {
            alertsReceived++;
            System.out.println("[App->" + username + "] ALERT  AQI=" + aqi +
                    " | Stay indoors! | Total alerts: " + alertsReceived);
        } else {
            System.out.println("[App->" + username + "] AQI=" + aqi + " | Safe ");
        }
    }
}

// Client
public class SmartCityAirQuality {
    public static void main(String[] args) {
        AirMonitor monitor = new AirMonitor();

        // Register subscribers
        monitor.register(new PurifierUnit("Bedroom"));
        monitor.register(new TrafficPanel("Downtown"));
        monitor.register(new MobileNotifier("Alice"));
        monitor.register(new MobileNotifier("Bob"));

        // Simulate AQI changes
        monitor.updateAqi(90);    // Normal
        monitor.updateAqi(140);   // Moderate
        monitor.updateAqi(200);   // Unhealthy
        monitor.updateAqi(280);   // Severe
    }
}
