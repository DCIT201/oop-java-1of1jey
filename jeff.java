abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean isAvailable;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        Objects.requireNonNull(vehicleId, "Vehicle ID cannot be null");
        Objects.requireNonNull(model, "Model cannot be null");
        if (baseRentalRate <= 0) {
            throw new IllegalArgumentException("Base rental rate must be greater than zero.");

        }
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.isAvailable = true;
    }

    public String getVehicleId() { return vehicleId; }
    public String getModel() { return model; }
    public double getBaseRentalRate() { return baseRentalRate; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }

    public abstract double calculateRentalCost(int days);
    public abstract boolean isAvailableForRental();
}

// Interface: Rentable
interface Rentable {
    void rent(Customer customer, int days);
    void returnVehicle();
}

// Concrete Class: Car
class Car extends Vehicle implements Rentable {
    private static final double DISCOUNT_RATE = 0.9;

    public Car(String vehicleId, String model, double baseRentalRate) {
        super(vehicleId, model, baseRentalRate);
    }

    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        return days > 7 ? cost * DISCOUNT_RATE : cost;
    }

    public boolean isAvailableForRental() {
        return isAvailable();
    }

    public void rent(Customer customer, int days) {
        if (!isAvailable()) {
            throw new IllegalStateException("Car is not available.");
        }
        setAvailable(false);
        System.out.printf("%s rented Car: %s for %d days at a cost of %.2f%n", 
            customer.getName(), getModel(), days, calculateRentalCost(days));
    }

    public void returnVehicle() {
        setAvailable(true);
        System.out.printf("Car returned: %s%n", getModel());
    }
}

// Supporting Class: Customer
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        if (customerId == null || name == null) {
            throw new IllegalArgumentException("Invalid customer details.");
        }
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
}

// Supporting Class: RentalAgency
class RentalAgency {
    private List<Vehicle> fleet = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public void listAvailableVehicles() {
        for (Vehicle vehicle : fleet) {
            if (vehicle.isAvailable()) {
                System.out.println(vehicle.getModel());
            }
        }
    }
}

// Main Class
public class VehicleRentalManagementSystem {
    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();
        Vehicle car = new Car("C123", "Honda Accord", 50);
        Customer customer = new Customer("CU01", "Jeff");

        agency.addVehicle(car);
        agency.listAvailableVehicles();

        ((Rentable) car).rent(customer, 5);
        agency.listAvailableVehicles();
        ((Rentable) car).returnVehicle();
    }
}
