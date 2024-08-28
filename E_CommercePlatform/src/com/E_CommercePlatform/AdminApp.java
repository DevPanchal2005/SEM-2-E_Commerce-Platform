package com.E_CommercePlatform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.E_CommercePlatform.Admin.LoginAdmin;
import com.E_CommercePlatform.Admin.SignupAdmin;

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
                    LoginAdmin loginAdmin = new LoginAdmin();
                    loginAdmin.login();
                    break;
                case 2:
                    SignupAdmin signupAdmin = new SignupAdmin();
                    signupAdmin.signup();
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
