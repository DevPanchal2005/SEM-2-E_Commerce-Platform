package com.E_CommercePlatform.Admin;

import java.util.*;

import com.E_CommercePlatform.AdminApp;
import com.E_CommercePlatform.Customer.SignupCustomer;
import com.E_CommercePlatform.Entities.Products;
import com.E_CommercePlatform.util.InputValidator;

import java.sql.*;

public class Admin extends AdminApp {

	private int adminID;

	static Connection con = AdminApp.con;
	static PreparedStatement pst;
	InputValidator validator = new InputValidator();
	static Scanner sc = new Scanner(System.in);

	public Admin(int adminID) {
		this.adminID = adminID;
	}

	public void AdminPage() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("WELCOME TO ADMIN SECTION\n");
		int ch;
		do {
			System.out.println("*****************************************************\n");
			System.out.println("1 - MANAGE PRODUCTS");
			System.out.println("2 - ADD CUSTOMERS");
			System.out.println("3 - REMOVE CUSTOMERS");
			System.out.println("4 - VIEW PROFILE");
			System.out.println("5 - EDIT PROFILE");
			System.out.println("6 - VIEW REGISTERED CUSTOMERS");
			System.out.println("7 - LOGOUT FROM SYSTEM");
			System.out.println("*****************************************************\n");
			System.out.print("Enter choice: ");
			ch = sc.nextInt();
			sc.nextLine(); // To consume newline

			switch (ch) {
				case 1:
					Products products = new Products();
					products.ProductsPage();
					break;
				case 2:
					addCustomer();
					break;
				case 3:
					removeCustomer();
					break;
				case 4:
					removeCustomer();
					break;
				case 5:
					editProfile(adminID);
					break;
				case 6:
					viewCustomers();
					break;
				case 7:
					System.out.println("Thank you");
					break;
				default:
					System.out.println("Wrong choice");
			}
		} while (ch != 6);
		sc.close();
	}

	public void viewAdminProfile(int adminId) {
		String query = "SELECT * FROM admin_details WHERE AdminID = ?";
		try {
			pst = con.prepareStatement(query);
			pst.setInt(1, adminId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				System.out.println("Admin Profile Details:");
				System.out.println("Admin ID: " + rs.getInt("AdminID"));
				System.out.println("Name: " + rs.getString("Name"));
				System.out.println("Age: " + rs.getInt("Age"));
				System.out.println("Email: " + rs.getString("Email"));
				System.out.println("Address: " + rs.getString("Address"));
				System.out.println("Contact Number: " + rs.getString("ContactNumber"));
			} else {
				System.out.println("No profile found for Admin ID: " + adminId);
			}
		} catch (Exception e) {
			System.out.println("Error while retrieving profile: " + e.getMessage());
		}
	}

	private void editProfile(int adminID) {
		try {
			String query = "UPDATE admininfo SET "
					+ "Name = COALESCE(NULLIF(?, ''), Name), "
					+ "Age = COALESCE(NULLIF(?, 0), Age), "
					+ "Email = COALESCE(NULLIF(?, ''), Email), "
					+ "Address = COALESCE(NULLIF(?, ''), Address), "
					+ "ContactNumber = COALESCE(NULLIF(?, ''), ContactNumber), "
					+ "password = COALESCE(NULLIF(?, ''), password) "
					+ "WHERE AdminID = ?";

			pst = con.prepareStatement(query);

			String name = validator.getStringInput("Enter new name (leave empty to keep current): ", 20);
			int age = validator.getIntInput("Enter age (enter 0 to keep current): ", 1, 3);
			String email="";
			String contact="";
			String password = "";
			if(validator.choiceMaker("Do You Want to Change Email")){
				email = validator.setValidEmail();
			}
			String address = validator.getStringInput("Enter new address (leave empty to keep current): ", 20);
			if(validator.choiceMaker("Do You Want to Change Phone Number")){
				contact = validator.setValidPhoneNumber();
			}
			if(validator.choiceMaker("Do You Want to Change Phone Number")){
				password = validator.encryptPassword(validator.setValidPassword());
			}

			pst.setString(1, name);
			pst.setInt(2, age);
			pst.setString(3, email);
			pst.setString(4, address);
			pst.setString(5, contact);
			pst.setString(6, password);
			pst.setInt(7, adminID);

			int rowsUpdated = pst.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Profile updated successfully!");
			} else {
				System.out.println("Profile update failed!");
			}

		} catch (Exception e) {
			System.out.println("Error while updating profile: " + e.getMessage());
		}
	}

	private static void viewCustomers() {

		try {
			pst = con.prepareStatement("select * from customer_details");
			ResultSet rs = pst.executeQuery();

			if (!rs.isBeforeFirst()) {
				System.out.println("NO CUSTOMERS AVAILABLE");
			} else {
				System.out.print(
						"**********************************************************************************************************************************************************************\n");
				System.out.printf("%-20s \t %-20s \t %-10s \t %-20s \t %-30s \t %-20s\n", "CUSTOMER_ID", "NAME", "AGE",
						"EMAIL", "ADDRESS", "CONTACT_NUMBER");
				System.out.println(
						"**********************************************************************************************************************************************************************\n");

				while (rs.next()) {
					int cid = rs.getInt("custID");
					String name = rs.getString("Name");
					int age = rs.getInt("Age");
					String email = rs.getString("Email");
					String addr = rs.getString("Address");
					String contact = rs.getString("ContactNumber");

					System.out.printf("%-20d \t %-20s \t %-10d \t %-20s \t %-30s \t %-20s\n", cid, name, age, email,
							addr, contact);
				}

				System.out.println(
						"*********************************************************************************************************************************************************************\n");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void addCustomer() {
		SignupCustomer signupCustomer = new SignupCustomer();
		signupCustomer.signup();
	}

	private static void removeCustomer() {
		try {
			pst = con.prepareStatement("select * from custinfo");
			ResultSet rs = pst.executeQuery();

			if (!rs.isBeforeFirst()) {
				System.out.println("NO CUSTOMERS AVAILABLE");
			} else {
				System.out.print("Enter customer ID to delete: ");
				int cid = sc.nextInt();
				sc.nextLine(); // To consume newline

				pst = con.prepareStatement("delete from custinfo where custID=?");

				pst.setInt(1, cid);

				if (pst.executeUpdate() != 0)
					System.out.println("CUSTOMER INFO DELETED SUCCESSFULLY!");
				else
					System.out.println("CUSTOMER INFO NOT FOUND!");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
