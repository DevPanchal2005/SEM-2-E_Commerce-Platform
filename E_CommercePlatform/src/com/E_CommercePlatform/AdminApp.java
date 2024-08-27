package com.E_CommercePlatform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.E_CommercePlatform.Customer.LoginCustomer;
import com.E_CommercePlatform.Customer.SignupCustomer;

public class AdminApp {
    public static Connection con;
    static PreparedStatement pst;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/E_Commerce", "root", "");

        System.out.println("WELCOME ADMIN TO ONLINE SHOPPING SYSTEM\n");
        int ch;
        do {
            System.out.print("*****************************************************\n");
            System.out.println("1 - LOGIN TO SYSTEM");
            System.out.println("2 - SIGN-UP TO SYSTEM");
            System.out.println("3 - EXIT");
            System.out.println("*****************************************************\n");
            System.out.print("Enter choice : ");
            ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    LoginCustomer loginCustomer = new LoginCustomer();
                    loginCustomer.login();
                    break;
                case 2:
                    SignupCustomer signupCustomer = new SignupCustomer();
                    signupCustomer.signup();
                    break;
                case 3:
                    System.out.println("Thank you for Visiting");
                    break;
                default:
                    System.out.println("Invalid Option");
                    break;
            }
        } while (ch != 4);
    }
}
