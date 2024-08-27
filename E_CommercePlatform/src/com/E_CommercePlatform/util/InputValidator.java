package com.E_CommercePlatform.util;

import java.util.*;
import java.util.regex.Pattern;

public class InputValidator {

    private static Scanner scanner = new Scanner(System.in);

    public int getIntInput(String prompt, int min, int maxLength) {
        int input;
        while (true) {
            System.out.print(prompt);
            String inputString = scanner.nextLine().trim();
            try {
                // Check if the input length exceeds the maximum allowed length
                if (inputString.length() > maxLength) {
                    System.out.println("Input length must not exceed " + maxLength + " digits.");
                    continue;
                }

                input = Integer.parseInt(inputString);

                // Check if the input is greater than or equal to the specified minimum
                if (input >= min) {
                    break;
                } else {
                    System.out.println("Input must be greater than or equal to " + min + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
        return input;
    }

    public float getFloatInput(String prompt, float min) {
        float input;
        while (true) {
            System.out.print(prompt);
            String inputString = scanner.nextLine().trim();
            try {
                input = Float.parseFloat(inputString);

                // Check if the input is greater than or equal to the specified minimum
                if (input >= min) {
                    break;
                } else {
                    System.out.println("Input must be greater than or equal to " + min + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid float value.");
            }
        }
        return input;
    }

    public String getStringInput(String prompt, int maxLength) {
        while (true) {
            System.out.print(prompt);
            String userInput = scanner.nextLine();

            if (userInput.length() <= maxLength) {
                return userInput;
            } else {
                System.out.println("Invalid input. Please enter a string of " + maxLength + " characters or shorter.");
                System.out.println();
            }
        }
    }

    public String setValidPassword() {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        // Password must contain at least one digit, one lowercase, one uppercase
        // letter, and be at least 8 characters long
        while (true) {
            String password = getStringInput(
                    "Enter password(Password must be at least 8 characters long, contain at least one digit, one lowercase letter, and one uppercase letter): ",
                    20);
            if (Pattern.matches(regex, password)) {
                return password;
            } else {
                System.out.println(
                        "Invalid password. Password must be at least 8 characters long, contain at least one digit, one lowercase letter, and one uppercase letter.");
                System.out.println();
            }
        }
    }

    public String encryptPassword(String password) {
        StringBuilder encryptedPassword = new StringBuilder();

        for (char ch : password.toCharArray()) {
            encryptedPassword.append((char) (ch + 13));
        }

        return encryptedPassword.toString();
    }

    public String setValidPhoneNumber() {
        String regex = "^[789]\\d{9}$"; // Indian phone number regex
        while (true) {
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();
            if (Pattern.matches(regex, phone)) {
                return phone;
            } else {
                System.out.println("Invalid phone number. Please enter a valid Indian phone number.");
                System.out.println();
            }
        }
    }

    public String setValidEmail() {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        while (true) {
            String email = getStringInput("Enter email: ", 20);
            if (Pattern.matches(regex, email)) {
                return email;
            } else {
                System.out.println("Invalid email. Please enter a valid email address.");
                System.out.println();
            }
        }
    }

    public boolean choiceMaker(String prompt) {
        while (true) {
            String choice = this.getStringInput(prompt, 1);
            if (choice.equalsIgnoreCase("Y")) {
                return true;
            } else if (choice.equalsIgnoreCase("N")) {
                return false;
            } else
                System.out.println("Invalid Input enter Y (YES) / N (NO) ");
        }
    }
}
