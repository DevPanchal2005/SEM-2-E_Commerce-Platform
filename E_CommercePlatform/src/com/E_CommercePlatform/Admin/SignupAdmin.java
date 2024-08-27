package com.E_CommercePlatform.Admin;

import java.sql.*;

import com.E_CommercePlatform.AdminApp;
import com.E_CommercePlatform.util.InputValidator;

public class SignupAdmin {
    Connection con = AdminApp.con;
    PreparedStatement pst;
    InputValidator validator = new InputValidator();

    public void signup() {
        String pass, name, num, addr, email;
        int age;
        System.out.println("\nWELCOME TO ADMIN REGISTRATION PAGE\n");
        System.out.println("*****************************************************\n");

        name = validator.getStringInput("Enter Name = ", 10);
        pass = validator.encryptPassword(validator.setValidPassword());
        age = validator.getIntInput("Enter age = ", 1, 10);
        num = validator.setValidPhoneNumber();
        addr = validator.getStringInput("Enter address = ", 20);
        email = validator.setValidEmail();

        // inserting data into database
        try {
            pst = con.prepareStatement(
                    "INSERT INTO `admininfo` (`password`, `Name`, `Age`, `Email`, `Address`, `ContactNumber`) VALUES ( ?, ?, ?, ?, ?, ?);");
            pst.setString(1, pass);
            pst.setString(2, name);
            pst.setInt(3, age);
            pst.setString(4, email);
            pst.setString(5, addr);
            pst.setString(6, num);

            if (pst.executeUpdate() > 0) {
                System.out.println("REGISTRATION DONE SUCCESSFULLY !\n");
            } else
                System.out.println("REGISTRATION FAILED !\n");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
