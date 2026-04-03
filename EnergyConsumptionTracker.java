import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * EnergyConsumptionTracker.java
 *
 * Single-file Java conversion of your C++ Energy Consumption Tracker.
 * - Use files: users.txt, households.txt, organizations.txt (format examples below)
 * - Uses OOP: City, Household, Organization, LoginManager, MainMenu
 *
 * Compile: javac EnergyConsumptionTracker.java
 * Run:     java EnergyConsumptionTracker
 */
public class EnergyConsumptionTracker {

    // Formatter for money values
    private static final DecimalFormat moneyFmt = new DecimalFormat("0.00");

    // Global city database
    private static final Map<String, City> cityDatabase = new HashMap<>();

    // Util: small loading animation (blocking)
    private static void loadingAnimation() {
        System.out.print("\nLoading");
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            System.out.flush();
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}
        }
        System.out.println("\n");
    }

    // City class
    static class City {
        String name;
        double waterCostPerLiter;
        double electricityCostPerUnit;
        double avgWaterUsage;
        double avgElectricityUsage;
        double pollutionIndex;
        int population;

        City(String n, double wc, double ec, double aw, double ae, double pi, int pop) {
            name = n;
            waterCostPerLiter = wc;
            electricityCostPerUnit = ec;
            avgWaterUsage = aw;
            avgElectricityUsage = ae;
            pollutionIndex = pi;
            population = pop;
        }
    }

    
    private static void initializeCityDatabase() {
        cityDatabase.put("Delhi", new City("Delhi", 2, 7.5, 4200, 190, 88.5, 19800000));
        cityDatabase.put("Mumbai", new City("Mumbai", 1.8, 7.2, 4100, 185, 85.2, 20400000));
        cityDatabase.put("Bangalore", new City("Bangalore", 2.4, 7.8, 4000, 200, 80.1, 13000000));
        cityDatabase.put("Chennai", new City("Chennai", 1.75, 7.4, 4050, 195, 78.3, 11000000));
        cityDatabase.put("Hyderabad", new City("Hyderabad", 1.5, 7.3, 4000, 180, 79.5, 10000000));
        cityDatabase.put("Kolkata", new City("Kolkata", 1.9, 7.1, 4300, 175, 83.7, 15000000));
    }

    // Household class
    static class Household {
        String username;
        String city;
        int electricityConsumed;
        int waterConsumed;
        boolean billPaid;

        // Load household from households.txt; returns true on success
        boolean loadHousehold(String uname, String cty) {
            File f = new File("households.txt");
            if (!f.exists()) {
                System.err.println("Error: households.txt not found.");
                return false;
            }
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNext()) {
                    String u = sc.next();
                    if (!sc.hasNext()) break;
                    String c = sc.next();
                    if (!sc.hasNextInt()) break;
                    int e = sc.nextInt();
                    if (!sc.hasNextInt()) break;
                    int w = sc.nextInt();
                    if (!sc.hasNext()) break;
                    String paidToken = sc.next();

                    boolean paid = parseBooleanToken(paidToken);

                    if (u.equals(uname) && c.equalsIgnoreCase(cty)) {
                        username = u;
                        city = c;
                        electricityConsumed = e;
                        waterConsumed = w;
                        billPaid = paid;
                        return true;
                    }
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error opening households.txt: " + ex.getMessage());
            }
            return false;
        }

        // Helper to accept "true"/"false" or "1"/"0"
        private boolean parseBooleanToken(String t) {
            t = t.trim().toLowerCase();
            if (t.equals("1") || t.equals("true") || t.equals("yes")) return true;
            return false;
        }

        void displayStatus() {
            City cityData = cityDatabase.getOrDefault(city, null);
            if (cityData == null) {
                System.out.println("City data not found for: " + city);
                return;
            }
            double waterBill = waterConsumed * cityData.waterCostPerLiter;
            double electricityBill = electricityConsumed * cityData.electricityCostPerUnit;

            System.out.println("\n--- Current Month Status ---");
            System.out.println("City: " + city);
            System.out.println("Electricity Consumed: " + electricityConsumed + " units");
            System.out.println("Water Consumed: " + waterConsumed + " liters");
            System.out.println("Electricity Bill: rupees " + moneyFmt.format(electricityBill));
            System.out.println("Water Bill: rupees " + moneyFmt.format(waterBill));
            System.out.println("Total Bill: rupees " + moneyFmt.format(electricityBill + waterBill));
            if (!billPaid) {
                System.out.println("** ALERT: Bill Payment Pending! **");
            } else {
                System.out.println("Bill Status: Paid");
            }

            System.out.println("\n[Benchmarks]");
            System.out.println("Electricity Benchmark: 200 units/month");
            System.out.println("Water Benchmark: ~4000 liters/month");

            if (electricityConsumed > 200)
                System.out.println(" You have exceeded the electricity benchmark! Try using energy-efficient appliances.");
            else
                System.out.println(" Electricity usage is within sustainable limits.");

            if (waterConsumed > 4000)
                System.out.println(" Water usage above recommended levels! Repair leaks, use water-saving techniques.");
            else
                System.out.println(" Water usage is within sustainable limits.");

            System.out.println();
            System.out.println("----WANT TO BE MORE RESPONSIBLE? FOLLOW THESE STEPS TO CONSERVE ENERGY----\n");
            System.out.println("Switch to LED lights: saves 80% energy.");
            System.out.println("Unplug devices :kill hidden 'phantom' power drains.");
            System.out.println("Set AC to 24 degC : ideal balance between comfort and savings.");
            System.out.println("Use full loads in washing machines and dishwashers.");
            System.out.println("Fix leaks immediately : save thousands of liters.");
            System.out.println("Install low-flow taps : cut water use by 30–50%.");
            System.out.println("Air-dry clothes instead of using the dryer.");
            System.out.println("Maximize daylight : keep lights off during daytime.");
            System.out.println("Use energy star appliances : long-term massive savings.");
            System.out.println("Track usage monthly : small corrections = big results.\n");
            exportBillReport();
        }
        void exportBillReport() {
            City cityData = cityDatabase.getOrDefault(city, null);
            if (cityData == null) return;

            double waterBill = waterConsumed * cityData.waterCostPerLiter;
            double electricityBill = electricityConsumed * cityData.electricityCostPerUnit;

            String filename = username + "_bill_report.txt";
            try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
                pw.println("===== Bill Report =====");
                pw.println("User: " + username);
                pw.println("City: " + city);
                pw.println("Electricity: " + electricityConsumed + " units — Rs. " + moneyFmt.format(electricityBill));
                pw.println("Water: " + waterConsumed + " liters — Rs. " + moneyFmt.format(waterBill));
                pw.println("Total: Rs. " + moneyFmt.format(electricityBill + waterBill));
                pw.println("Status: " + (billPaid ? "Paid" : "PENDING"));
                System.out.println("\nReport exported to: " + filename);
            } catch (IOException e) {
                System.out.println("Error exporting report: " + e.getMessage());
            }
        }
    
    }

    // Organization class
    static class Organization {
        String name;
        String license;
        int[] electricity = new int[5];
        int[] water = new int[5];

        boolean loadOrganization(String orgName, String lic) {
            File f = new File("organizations.txt");
            if (!f.exists()) {
                System.err.println("Error: organizations.txt not found.");
                return false;
            }
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNext()) {
                    String nm = sc.next();
                    if (!sc.hasNext()) break;
                    String licToken = sc.next();

                    int[] e = new int[5];
                    int[] w = new int[5];
                    boolean ok = true;
                    for (int i = 0; i < 5; i++) {
                        if (!sc.hasNextInt()) { ok = false; break; }
                        e[i] = sc.nextInt();
                        if (!sc.hasNextInt()) { ok = false; break; }
                        w[i] = sc.nextInt();
                    }
                    if (!ok) break;

                    if (nm.equalsIgnoreCase(orgName) && licToken.equals(lic)) {
                        name = nm;
                        license = licToken;
                        electricity = e;
                        water = w;
                        return true;
                    }
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error opening organizations.txt: " + ex.getMessage());
            }
            return false;
        }

        void displayStatus() {
            System.out.println("\n--- Past 5 Months Consumption ---");
            System.out.printf("%-10s %-20s %-20s%n", "Month", "Electricity (units)", "Water (liters)");
            System.out.println("------------------------------------------------------");
            
            int totalElec = 0, totalWater = 0; 
            
            
            for (int i = 0; i < 5; i++) {
                System.out.printf("%-10s %-20d %-20d%n", "Month " + (i + 1), electricity[i], water[i]);
                totalElec += electricity[i];
                totalWater += water[i];
            }

            
            double avgElec = totalElec / 5.0;
            double avgWater = totalWater / 5.0;
            System.out.printf("%nAverage Monthly Electricity: %.1f units%n", avgElec);
            System.out.printf("Average Monthly Water: %.1f liters%n", avgWater);

            
            if (avgElec > 1000)
                System.out.println("WARNING: Average electricity usage exceeds recommended organizational limit.");
            if (avgWater > 20000)
                System.out.println("WARNING: Average water usage exceeds recommended organizational limit.");

            System.out.println();
            System.out.println("----WANT A SUSTAINABLE ORGANISATION? FOLLOW THESE STEPS TO CONSERVE ENERGY----\n");
            System.out.println("Conduct regular energy audits to spot wastage early.");
            System.out.println("Switch to smart lighting and motion sensors.");
            System.out.println("Promote remote work — cut office energy footprint.");
            System.out.println("Invest in energy-efficient HVAC systems.");
            System.out.println("Implement water recycling and rainwater harvesting.");
            System.out.println("Automate equipment shutdown after work hours.");
            System.out.println("Use renewable energy sources where possible.");
            System.out.println("Create awareness campaigns for employees.");
            System.out.println("Set clear sustainability targets and track them monthly.\n");
        }
    }

    // Login manager
    static class LoginManager {
        boolean login(String role, Scanner console) {
            System.out.println("\n--- " + role + " Login ---");
            System.out.print("Enter Username: ");
            String username = console.nextLine().trim();
            System.out.print("Enter Password: ");
            String password = console.nextLine().trim();

            File f = new File("users.txt");
            if (!f.exists()) {
                System.err.println("Error: users.txt not found.");
                return false;
            }
            try (Scanner sc = new Scanner(f)) {
                while (sc.hasNext()) {
                    String u = sc.next();
                    if (!sc.hasNext()) break;
                    String p = sc.next();
                    if (!sc.hasNext()) break;
                    String r = sc.next();

                    if (u.equals(username) && p.equals(password) && r.equalsIgnoreCase(role)) {
                        System.out.println("\nLogin Successful!");
                        loadingAnimation();
                        return true;
                    }
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Error opening users.txt: " + ex.getMessage());
            }
            System.out.println("\nLogin Failed. Please try again.");
            return false;
        }
    }

    
    private static void compareCities(Scanner console) {
        System.out.println("\nEnter First City Name:");
        String city1 = console.nextLine().trim();
        System.out.println("Enter Second City Name:");
        String city2 = console.nextLine().trim();

        City c1 = cityDatabase.get(city1);
        City c2 = cityDatabase.get(city2);
        if (c1 == null || c2 == null) {
            System.out.println("One or both cities not found!");
            return;
        }

        System.out.println("\n--- City Comparison ---");
        System.out.printf("%-20s %-20s %-20s%n", "Parameter", city1, city2);
        System.out.println("----------------------------------------------------------");
        System.out.printf("%-20s %-20.2f %-20.2f%n", "Water Cost/Liter", c1.waterCostPerLiter, c2.waterCostPerLiter);
        System.out.printf("%-20s %-20.2f %-20.2f%n", "Electricity Cost/Unit", c1.electricityCostPerUnit, c2.electricityCostPerUnit);
        System.out.printf("%-20s %-20.1f %-20.1f%n", "Avg Water Usage", c1.avgWaterUsage, c2.avgWaterUsage);
        System.out.printf("%-20s %-20.1f %-20.1f%n", "Avg Electricity Usage", c1.avgElectricityUsage, c2.avgElectricityUsage);
        System.out.printf("%-20s %-20.1f %-20.1f%n", "Pollution Index", c1.pollutionIndex, c2.pollutionIndex);
        System.out.printf("%-20s %-20d %-20d%n", "Population", c1.population, c2.population);
    }
    
    
    
    private static void rankCitiesByMetric(Scanner console) {
        System.out.println("\nRank cities by:");
        System.out.println("1. Water Cost  2. Electricity Cost  3. Pollution Index  4. Population");
        System.out.print("Choice: ");
        String input = console.nextLine().trim();

        List<City> cities = new ArrayList<>(cityDatabase.values());

        switch (input) {
            case "1": cities.sort(Comparator.comparingDouble(c -> c.waterCostPerLiter)); break;
            case "2": cities.sort(Comparator.comparingDouble(c -> c.electricityCostPerUnit)); break;
            case "3": cities.sort(Comparator.comparingDouble(c -> c.pollutionIndex)); break;
            case "4": cities.sort(Comparator.comparingInt(c -> c.population)); break;
            default: System.out.println("Invalid choice."); return;
        }

        System.out.printf("%n%-15s %-20s %-20s %-15s %-12s%n",
            "City", "Water Cost/L", "Elec Cost/Unit", "Pollution Idx", "Population");
        System.out.println("------------------------------------------------------------------------");
        for (City c : cities) {
            System.out.printf("%-15s %-20.2f %-20.2f %-15.1f %-12d%n",
                c.name, c.waterCostPerLiter, c.electricityCostPerUnit, c.pollutionIndex, c.population);
        }
    }

    
    private static void mainMenu() {
        Scanner console = new Scanner(System.in);
        LoginManager loginManager = new LoginManager();
        int choice = -1;
        do {
            System.out.println("\n========== Energy Consumption Tracker ==========");
            System.out.println("1. Check Household Status");
            System.out.println("2. Check Organizational Status");
            System.out.println("3. General Knowledge Area (City Comparison)");
            System.out.println("4. Rank Cities by Metric"); 
            System.out.println("5. Exit");                  
            System.out.print("\nEnter your choice: ");
            String input = console.nextLine().trim();
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    if (loginManager.login("household", console)) {
                        System.out.print("\nEnter Username again: ");
                        String username = console.nextLine().trim();
                        System.out.print("Enter City: ");
                        String city = console.nextLine().trim();

                        Household h = new Household();
                        if (h.loadHousehold(username, city)) {
                            h.displayStatus();
                        } else {
                            System.out.println("Household data not found.");
                        }
                    }
                    break;

                case 2:
                    if (loginManager.login("organization", console)) {
                        System.out.print("\nEnter Organization Name: ");
                        String name = console.nextLine().trim();
                        System.out.print("Enter License Number: ");
                        String license = console.nextLine().trim();

                        Organization o = new Organization();
                        if (o.loadOrganization(name, license)) {
                            o.displayStatus();
                        } else {
                            System.out.println("Organization data not found.");
                        }
                    }
                    break;

                case 3:
                    compareCities(console);
                    break;
                    
                case 4:
                    rankCitiesByMetric(console); 
                    break;

                case 5: 
                    System.out.println("\nThank you for using Energy Consumption Tracker!");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 5); 
    }
    

    // Main
    public static void main(String[] args) {
        initializeCityDatabase();
        mainMenu();
    }
}
