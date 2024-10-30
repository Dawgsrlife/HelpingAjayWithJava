// Importing necessary classes for file handling and user input
import java.io.*;
import java.util.*;

public class TaxCalculator {
    public static void main(String[] args) {
        // Specify the number of records to read from the file
        int inputSize = 20;

        // Initialize arrays to hold taxpayer information
        String[] names = new String[inputSize];        // Array to store names
        String[] companies = new String[inputSize];    // Array to store company names
        double[] salaries = new double[inputSize];     // Array to store annual salaries
        double[] taxesOwed = new double[inputSize];    // Array to store calculated tax amounts

        try {
            // Set up file reading and writing
            BufferedReader br = new BufferedReader(new FileReader("taxpayer.txt")); // Reads taxpayer info
            BufferedWriter bw = new BufferedWriter(new FileWriter("taxInfo.txt"));  // Writes tax info

            // Process each record by reading and calculating tax information
            for (int i = 0; i < inputSize; i++) {
                // Read name, company, and salary from taxpayer.txt
                names[i] = br.readLine();                   // Store name
                companies[i] = br.readLine();               // Store company name
                salaries[i] = Double.parseDouble(br.readLine());  // Convert salary from string to double

                // Determine tax rate based on salary
                double taxRate = (salaries[i] <= 10000000.00) ? 40.0 : 53.0; // 40% for ≤ 10M, 53% otherwise
                taxesOwed[i] = calcTax(salaries[i], taxRate);                // Calculate tax owed

                // Write the taxpayer's information and calculated tax to taxInfo.txt
                bw.write(names[i] + "\n" + companies[i] + "\n" +
                        String.format("%.2f", salaries[i]) + "\n" +
                        String.format("%.2f", taxesOwed[i]) + "\n\n");
            }

            // Close the files after processing all records
            br.close();
            bw.close();

            // Inform user and display records in console
            System.out.println("Records stored in taxInfo.txt");
            for (int i = 0; i < inputSize; i++) {
                System.out.printf("Name: %s\nCompany: %s\nSalary: %.2f\nTax Owed: %.2f\n\n",
                        names[i], companies[i], salaries[i], taxesOwed[i]);
            }

            // Sort records by salary in ascending order
            sortBySalary(names, companies, salaries, taxesOwed); // Calls sorting function

            // Save sorted information to a new file
            saveSortedInfo(names, companies, salaries, taxesOwed);

            // Search for a taxpayer by name
            Scanner scanner = new Scanner(System.in); // For user input
            System.out.print("Enter the name of the taxpayer to search: ");
            String searchName = scanner.nextLine();
            searchTaxpayer(names, companies, salaries, taxesOwed, searchName); // Call search function
            scanner.close(); // Close scanner

        } catch (IOException e) {
            // Handles exceptions for file I/O operations
            System.err.println("File I/O error: " + e.getMessage());
        }
    }

    // Method to calculate tax based on salary and tax rate
    public static double calcTax(double salary, double taxRate) {
        return salary * taxRate / 100; // Returns calculated tax by applying the rate to salary
    }

    // Method to sort taxpayer arrays by salary in ascending order
    public static void sortBySalary(String[] names, String[] companies, double[] salaries, double[] taxesOwed) {
        // Bubble Sort algorithm: iterates through list to swap elements until sorted
        // Suitable for small lists due to simplicity, though not efficient for large datasets
        for (int i = 0; i < salaries.length - 1; i++) {         // Outer loop through each element
            for (int j = i + 1; j < salaries.length; j++) {     // Inner loop to compare each pair
                if (salaries[i] > salaries[j]) {                // Swap if out of order
                    // Swap salary values
                    double tempSalary = salaries[i];
                    salaries[i] = salaries[j];
                    salaries[j] = tempSalary;

                    // Swap taxes owed values
                    double tempTax = taxesOwed[i];
                    taxesOwed[i] = taxesOwed[j];
                    taxesOwed[j] = tempTax;

                    // Swap names to keep corresponding data together
                    String tempName = names[i];
                    names[i] = names[j];
                    names[j] = tempName;

                    // Swap companies to keep data consistent across arrays
                    String tempCompany = companies[i];
                    companies[i] = companies[j];
                    companies[j] = tempCompany;
                }
            }
        }
    }

    // Method to save sorted information to a new file, "sortedTaxInfo.txt"
    public static void saveSortedInfo(String[] names, String[] companies, double[] salaries, double[] taxesOwed) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("sortedTaxInfo.txt"))) {
            // Write sorted data to the file in the specified format
            for (int i = 0; i < names.length; i++) {
                bw.write(names[i] + "\n" + companies[i] + "\n" +
                        String.format("%.2f", salaries[i]) + "\n" +
                        String.format("%.2f", taxesOwed[i]) + "\n\n");
            }
            System.out.println("Sorted records saved to sortedTaxInfo.txt");
        } catch (IOException e) {
            // Handles exceptions for file writing errors
            System.err.println("File I/O error: " + e.getMessage());
        }
    }

    // Method to search for a taxpayer by name and display their information
    public static void searchTaxpayer(String[] names, String[] companies, double[] salaries, double[] taxesOwed, String searchName) {
        boolean found = false; // Flag to indicate if taxpayer is found

        // Loop through names to find the search term
        for (int i = 0; i < names.length; i++) {
            if (names[i].equalsIgnoreCase(searchName)) { // Check if name matches ignoring case
                // Print the found taxpayer's information
                System.out.printf("Name: %s\nCompany: %s\nSalary: %.2f\nTax Owed: %.2f\n",
                        names[i], companies[i], salaries[i], taxesOwed[i]);
                found = true; // Set found to true to stop further searching
                break;        // Exit the loop once taxpayer is found
            }
        }

        if (!found) {
            // Inform user if taxpayer is not found in the data
            System.out.println("Taxpayer not found.");
        }
    }
}
