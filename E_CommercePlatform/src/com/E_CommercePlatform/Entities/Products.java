package com.E_CommercePlatform.Entities;

import java.sql.*;
import java.util.Scanner;

import com.E_CommercePlatform.CustomerApp;
import com.E_CommercePlatform.util.InputValidator;

public class Products {

	private int pid;

	private static final Scanner sc = new Scanner(System.in);
	Connection con = CustomerApp.con;
	PreparedStatement pst;
	InputValidator validator = new InputValidator();

	public void ProductsPage() {
		Products products = new Products();
		System.out.println("\nWELCOME TO PRODUCTS MANAGEMENT PAGE\n");
		int ch;
		do {
			System.out.println("*****************************************************\n");
			System.out.println("1 - ADD PRODUCTS");
			System.out.println("2 - REMOVE PRODUCTS");
			System.out.println("3 - ALTER PRODUCT INFO");
			System.out.println("4 - VIEW ALL PRODUCTS");
			System.out.println("5 - SEARCH A PARTICULAR PRODUCT");
			System.out.println("6 - EXIT PAGE");
			System.out.println("*****************************************************\n");
			System.out.print("Enter choice: ");
			ch = sc.nextInt();
			sc.nextLine(); // Consume newline

			switch (ch) {
				case 1 -> products.addProducts();
				case 2 -> products.removeProducts();
				case 3 -> products.alterProduct();
				case 4 -> products.viewProducts();
				case 5 -> products.searchProduct();
				case 6 -> System.out.println("Thank you");
				default -> System.out.println("Wrong choice");
			}
		} while (ch != 6);
	}

	private void alterProduct() {
		try {
			System.out.print("Enter product ID to update info: ");
			int productId = sc.nextInt();

			pst = con.prepareStatement("SELECT * FROM product_details WHERE productID = ?");
			pst.setInt(1, productId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				String name = rs.getString("Name");
				String type = rs.getString("Type");
				int quantity = rs.getInt("Quantity");
				float price = rs.getFloat("Price");

				System.out.println("FETCHED PRODUCT INFO:\n");
				System.out.printf("Product ID   = %-5d\n", productId);
				System.out.printf("Product Name = %-20s\n", name);
				System.out.printf("Product Type = %-20s\n", type);
				System.out.printf("Quantity     = %-5d\n", quantity);
				System.out.printf("Price        = %-10.2f\n", price);

				String newName = validator.getStringInput("Enter new product name (leave empty to keep current): ", 30);
				String newType = validator.getStringInput("Enter new product type (leave empty to keep current): ", 30);
				int newQuantity = validator.getIntInput("Enter new quantity (leave empty to keep current): ", 0, 10);
				float newPrice = validator.getFloatInput("Enter new price (leave empty to keep current): ", 0.1f);

				String updateQuery = "UPDATE products SET "
						+ "Name = COALESCE(NULLIF(?, ''), Name), "
						+ "Type = COALESCE(NULLIF(?, ''), Type), "
						+ "Quantity = COALESCE(NULLIF(?, ''), Quantity), "
						+ "Price = COALESCE(NULLIF(?, ''), Price) "
						+ "WHERE productID = ?";

				pst = con.prepareStatement(updateQuery);
				pst.setString(1, newName);
				pst.setString(2, newType);
				pst.setInt(3, newQuantity);
				pst.setFloat(4, newPrice);
				pst.setInt(5, productId);

				int rowsUpdated = pst.executeUpdate();
				if (rowsUpdated > 0) {
					System.out.println("Product information updated successfully!");
				} else {
					System.out.println("Product update failed!");
				}
			} else {
				System.out.println("Product not found!");
			}
		} catch (Exception e) {
			System.out.println("Error while updating product information: " + e.getMessage());
		}
	}

	private void searchProduct() {
		int productFound = 0;
		String choice;
		try {
			pst = con.prepareStatement("select * from product_details");
			ResultSet rs = pst.executeQuery();

			if (rs.last()) {
				rs.beforeFirst();
			} else {
				System.out.println("NO PRODUCTS AVAILABLE");
				return;
			}

			do {
				System.out.print("Enter product ID to search: ");
				pid = sc.nextInt();
				sc.nextLine(); // Consume newline
				pst = con.prepareStatement("select * from product_details where productID=?");
				pst.setInt(1, pid);
				ResultSet rs1 = pst.executeQuery();
				productFound = 0;

				while (rs1.next()) {
					System.out.printf("Product ID   =  %-5d\n", rs1.getInt("productID"));
					System.out.printf("Product Name =  %-20s\n", rs1.getString("Name"));
					System.out.printf("Product Type =  %-20s\n", rs1.getString("Type"));
					System.out.printf("Quantity     =  %-5d\n", rs1.getInt("Quantity"));
					System.out.printf("Price        =  %-10.2f\n", rs1.getFloat("Price"));
					productFound = 1;
				}

				if (productFound == 0)
					System.out.println("PRODUCT NOT FOUND!");

				System.out.print("Do you want to continue, press Y for 'yes' N for 'no': ");
				choice = sc.nextLine();
			} while (choice.equalsIgnoreCase("Y"));
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println();
	}

	private void removeProducts() {
		String choice;
		try {
			pst = con.prepareStatement("delete from products where productID=?");

			do {
				System.out.print("Enter product ID to delete: ");
				pid = sc.nextInt();
				sc.nextLine(); // Consume newline
				pst.setInt(1, pid);

				if (pst.executeUpdate() == 0)
					System.out.println("PRODUCT NOT FOUND!");
				else
					System.out.println("PRODUCT DELETED SUCCESSFULLY!");

				System.out.print("Do you want to continue (Y for YES, N for NO): ");
				choice = sc.nextLine();
			} while (choice.equalsIgnoreCase("Y"));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void addProducts() {
		String choice;
		System.out.println("*****************************************************\n");
		try {
			pst = con.prepareStatement("INSERT INTO products(Name, Type, Quantity, Price) VALUES(?,?,?,?)");
			do {
				String name = validator.getStringInput("Enter Name: ", 20); // Assuming max length 50 for product name
				String type = validator.getStringInput("Enter Type: ", 20); // Assuming max length 20 for product type
				int qty = validator.getIntInput("Enter Quantity: ", 1, 10);
				float price = validator.getFloatInput("Enter Price: ", 0.01f);

				pst.setString(1, name);
				pst.setString(2, type);
				pst.setInt(3, qty);
				pst.setFloat(4, price);

				if (pst.executeUpdate() > 0) {
					System.out.println("Product Added!");
				}

				choice = validator.getStringInput("Do you want to continue, press Y for 'yes' N for 'no': ", 1);
			} while (choice.equalsIgnoreCase("Y"));
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println();
	}

	private void viewProducts() {
		int totalProducts = 0;
		try {
			pst = con.prepareStatement("select * from product_details");
			ResultSet rs = pst.executeQuery();

			if (rs.last()) {
				totalProducts = rs.getRow();
				rs.beforeFirst();
			}

			if (totalProducts == 0) {
				System.out.println("NO PRODUCTS AVAILABLE");
			} else {
				System.out.println("AVAILABLE PRODUCTS ARE:\n");
				System.out
						.println("PRODUCT ID   PRODUCT NAME             PRODUCT TYPE           QUANTITY        PRICE");
				while (rs.next()) {
					System.out.printf("%-12d%-25s%-25s%-10d%-10.2f\n", rs.getInt("productID"), rs.getString("Name"),
							rs.getString("Type"),
							rs.getInt("Quantity"), rs.getFloat("Price"));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println();
	}

}
