package com.E_CommercePlatform.Customer;

import java.sql.*;
import java.util.*;

import com.E_CommercePlatform.CustomerApp;
import com.E_CommercePlatform.util.ArrayList;
import com.E_CommercePlatform.util.InputValidator;

public class LoginCustomer {

    Connection con = CustomerApp.con;
    PreparedStatement pst;
    InputValidator validator = new InputValidator();
    Scanner sc = new Scanner(System.in);

    public void login() throws Exception {
        System.out.println("\nWELCOME CUSTOMER TO LOGIN PAGE\n");
        System.out.println("*****************************************************\n");

        // Initialize lists to store user data from the database
        ArrayList<Integer> idList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();
        ArrayList<String> emailList = new ArrayList<>();

        // Retrieve login info from the database
        pst = con.prepareStatement("SELECT * FROM customer_credentials");
        ResultSet rs = pst.executeQuery();

        // Store data into lists
        while (rs.next()) {
            idList.add(rs.getInt("custID"));
            passwordList.add(rs.getString("password"));
            emailList.add(rs.getString("email"));
        }

        // Variables to store user input and validation status
        Scanner sc = new Scanner(System.in);
        boolean isValid = false;
        int attempts = 0;
        int customerId = -1;
        String password;

        do {
            // Prompt user for ID and password
            System.out.print("Enter cUSTOMER ID: ");
            customerId = sc.nextInt();
            sc.nextLine(); // Consume newline character

            System.out.print("Enter PASSWORD: ");
            password = validator.encryptPassword(sc.nextLine());

            // Validate credentials
            int idIndex = idList.indexOf(customerId);

            if (idIndex == -1) {
                System.out.println("User ID not found...");
                System.out.print("Have you forgotten your User ID (Y for Yes, N for No): ");
                if (sc.next().equalsIgnoreCase("Y")) {
                    System.out.print("Enter your registered email ID: ");
                    sc.nextLine(); // Consume the newline character
                    String emailId = sc.nextLine();
                    int emailIndex = emailList.indexOf(emailId);

                    if (emailIndex != -1) {
                        customerId = idList.get(emailIndex);
                        System.out.println("Your User ID is: " + customerId);
                    } else {
                        System.out.println("Email ID not found in the system.");
                    }
                }
            } else {
                int passwordIndex = passwordList.indexOf(password);

                if (idIndex == passwordIndex) {
                    isValid = true;
                    System.out.println("Login successful!");
                } else {
                    System.out.println("INVALID CREDENTIALS, PLEASE TRY AGAIN!");
                }
            }

            if (!isValid) {
                attempts++;
                if (attempts >= 3) {
                    System.out.println("Maximum attempts reached. Exiting...");
                    sc.close();
                    return; // Exit the method if the maximum number of attempts is reached
                } else {
                    // Ask if the user wants to retry
                    System.out.print("Do you want to continue (Y for Yes, N for No): ");
                    String choice = sc.next();
                    sc.nextLine(); // Consume the newline character
                    if (choice.equalsIgnoreCase("N")) {
                        sc.close();
                        return; // Exit the method if the user chooses not to retry
                    }
                }
            }
        } while (!isValid);

        Customer customer = new Customer(customerId);
        customer.CustomerPage();
        sc.close();
    }
}
