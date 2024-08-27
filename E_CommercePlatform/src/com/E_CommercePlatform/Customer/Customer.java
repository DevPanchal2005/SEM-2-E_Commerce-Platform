package com.E_CommercePlatform.Customer;

import com.E_CommercePlatform.CustomerApp;
import com.E_CommercePlatform.util.*;
import com.E_CommercePlatform.Entities.Cart;
import com.E_CommercePlatform.Entities.Payment;

import java.util.Scanner;
import java.io.IOException;
import java.sql.*;

public class Customer extends CustomerApp {
	private int customerID;
	private Cart cart = new Cart();
	private int cartFlag = 0;
	private int billPaidFlag = 0;
	private int checkFlag = -1;
	Scanner sc = new Scanner(System.in);
	static Connection con = CustomerApp.con;
	static PreparedStatement pst;
	InputValidator validator = new InputValidator();

	private ArrayList<Integer> productIds = new ArrayList<>();
	private ArrayList<String> productNames = new ArrayList<>();
	private ArrayList<String> productTypes = new ArrayList<>();
	private ArrayList<Integer> productQuantities = new ArrayList<>();
	private ArrayList<Float> productPrices = new ArrayList<>();

	private int totalProducts;

	public Customer(int custID) {
		customerID = custID;
		cart = new Cart();
		billPaidFlag = 0;
		cartFlag = 0;
	}

	public void CustomerPage() throws IOException, SQLException {
		totalProducts = this.initializeProducts();
		System.out.println("WELCOME TO CUSTOMER SECTION\n");
		int ch;
		do {
			System.out.println("*****************************************************\n");
			System.out.println("1 - VIEW PRODUCTS LIST");
			System.out.println("2 - SEARCH A PRODUCT NAMEWISE");
			System.out.println("3 - SEARCH PRODUCTS TYPEWISE");
			System.out.println("4 - ADD PRODUCT TO CART");
			System.out.println("5 - REMOVE PRODUCT FROM CART");
			System.out.println("6 - VIEW CART");
			System.out.println("7 - PROCEED TO PAYMENT");
			System.out.println("8 - VIEW PROFILE");
			System.out.println("9 - EDIT PROFILE");
			System.out.println("10 - LOGOUT FROM SYSTEM");
			System.out.println("*****************************************************\n");
			System.out.print("Enter choice: ");
			ch = sc.nextInt();
			sc.nextLine(); // consume newline

			switch (ch) {
				case 1 -> this.viewProducts();
				case 2 -> this.searchNameWise();
				case 3 -> this.searchTypeWise();
				case 4 -> this.addProductsToCart();
				case 5 -> {
					System.out.print("ENTER PRODUCT ID TO REMOVE FROM CART: ");
					int removeProductId = sc.nextInt();
					sc.nextLine(); // consume newline
					cart.removeFromCart(removeProductId);
					this.updateArrayList();
				}
				case 6 -> cart.viewCart();
				case 7 -> this.proceedPayment(cart);
				case 8 -> viewProfile(customerID);
				case 9 -> editProfile(customerID);
				case 10 -> ch = this.checkExit();
				default -> System.out.println("Wrong choice");
			}
		} while (ch != 9);
	}

	private int initializeProducts() {
		int totalProducts = 0;
		try {
			pst = con.prepareStatement("SELECT * FROM view_all_products");
			ResultSet rs = pst.executeQuery();

			if (rs.last()) {
				totalProducts = rs.getRow();
				rs.beforeFirst();
			}
			if (totalProducts == 0) {
				return 0;
			} else {
				while (rs.next()) {
					productIds.add(rs.getInt("productID"));
					productNames.add(rs.getString("productName"));
					productTypes.add(rs.getString("productType"));
					productQuantities.add(rs.getInt("Quantity"));
					productPrices.add(rs.getFloat("Price"));
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return totalProducts;
	}

	private void viewProducts() {
		if (totalProducts == 0) {
			System.out.println("PRODUCTS NOT AVAILABLE!");
		} else {
			System.out.println(
					"***********************************************************************************************************************\n");
			System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID", "Product_Name",
					"Product_Type", "Product_Quantity", "Product_Price");
			System.out.println(
					"***********************************************************************************************************************\n");
			for (int i = 0; i < productIds.size(); i++) {
				if (productQuantities.get(i) != 0)
					System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", productIds.get(i),
							productNames.get(i),
							productTypes.get(i), productQuantities.get(i), productPrices.get(i));
				else
					System.out.printf("%-20d \t %-20s \t %-20s \t %-20s \t %-20f\n", productIds.get(i),
							productNames.get(i),
							productTypes.get(i), "NOT IN STOCK", productPrices.get(i));
			}
			System.out.println(
					"***********************************************************************************************************************\n");
		}
	}

	private void searchNameWise() {
		if (totalProducts == 0) {
			System.out.println("PRODUCTS NOT AVAILABLE!");
		} else {
			String productName;
			String choice;
			do {
				System.out.print("ENTER PRODUCT NAME TO SEARCH: ");
				productName = sc.nextLine();
				int index = productNames.indexOf(productName);
				if (index == -1) {
					System.out.println("PRODUCT NOT FOUND!");
				} else {
					System.out.println("PRODUCT DETAILS ARE:\n");
					System.out.printf("PRODUCT ID         = %-5d\n", productIds.get(index));
					System.out.printf("PRODUCT NAME       = %-5s\n", productNames.get(index));
					System.out.printf("PRODUCT TYPE       = %-5s\n", productTypes.get(index));
					if (productQuantities.get(index) != 0)
						System.out.printf("PRODUCT QUANTITY   = %-5d\n", productQuantities.get(index));
					else
						System.out.printf("PRODUCT QUANTITY   = NOT IN STOCK\n");
					System.out.printf("PRODUCT PRICE      = %-5f\n", productPrices.get(index));
				}
				System.out.print("DO YOU WANT TO SEARCH AGAIN (Y for yes): ");
				choice = sc.nextLine();
			} while (choice.equalsIgnoreCase("Y"));
		}
	}

	private void searchTypeWise() {
		if (totalProducts == 0) {
			System.out.println("PRODUCTS NOT AVAILABLE!");
		} else {
			String search;
			String choice;
			do {
				System.out.print("ENTER PRODUCT TYPE TO SEARCH: ");
				search = sc.nextLine();
				System.out.println(
						"***********************************************************************************************************************\n");
				System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID", "Product_Name",
						"Product_Type", "Product_Quantity", "Product_Price");
				System.out.println(
						"***********************************************************************************************************************\n");
				int productsFound = 0;
				for (int i = 0; i < productTypes.size(); i++) {
					if (productTypes.get(i).equalsIgnoreCase(search)) {
						productsFound = 1;
						if (productQuantities.get(i) != 0)
							System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", productIds.get(i),
									productNames.get(i),
									productTypes.get(i), productQuantities.get(i), productPrices.get(i));
						else
							System.out.printf("%-20d \t %-20s \t %-20s \t %-20s \t %-20f\n", productIds.get(i),
									productNames.get(i),
									productTypes.get(i), "NOT IN STOCK", productPrices.get(i));
					}
				}
				if (productsFound == 0)
					System.out.println("PRODUCT NOT FOUND!");
				System.out.print("DO YOU WANT TO SEARCH AGAIN (Y for yes): ");
				choice = sc.nextLine();
			} while (choice.equalsIgnoreCase("Y"));
		}
	}

	private void addProductsToCart() throws IOException {
		String userChoice;
		do {
			System.out.print("ENTER PRODUCT ID TO ADD TO CART: ");
			int productId = sc.nextInt();
			sc.nextLine(); // consume newline

			int availableQuantity = checkProductQuantity(productId);
			if (availableQuantity == -1) {
				System.out.println("PRODUCT NOT FOUND!");
			} else {
				System.out.println("QUANTITY AVAILABLE = " + availableQuantity);
				System.out.print("ENTER QUANTITY TO PURCHASE: ");
				int purchaseQuantity = sc.nextInt();
				sc.nextLine(); // consume newline

				if (purchaseQuantity > availableQuantity) {
					System.out.println("STOCK NOT AVAILABLE");
				} else {
					updateQty(purchaseQuantity, productId);

					String productName = productNames.get(productIds.indexOf(productId));
					String productType = productTypes.get(productIds.indexOf(productId));
					float totalPrice = purchaseQuantity * productPrices.get(productIds.indexOf(productId));

					cart.addToCart(productId, productName, productType, purchaseQuantity, totalPrice);
					cartFlag = 1;
				}
			}
			System.out.print("DO YOU WANT TO ADD ANOTHER PRODUCT (Y for yes): ");
			userChoice = sc.nextLine();
		} while (userChoice.equalsIgnoreCase("Y"));
	}

	private int checkProductQuantity(int productId) {
		int availableQuantity = productIds.indexOf(productId);
		return availableQuantity != -1 ? productQuantities.get(availableQuantity) : -1;
	}

	private void updateArrayList() {
		productIds.clear();
		productNames.clear();
		productTypes.clear();
		productQuantities.clear();
		productPrices.clear();
		initializeProducts();
	}

	private void proceedPayment(Cart cart) throws IOException, SQLException {
		if (cartFlag == 1) {
			String customerName = "";
			String customerAddress = "";
			String customerPhoneNum = "";
			try {
				pst = con.prepareStatement("SELECT * FROM custinfo WHERE custID = ?");
				pst.setInt(1, customerID);
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					customerName = rs.getString("Name");
					customerAddress = rs.getString("Address");
					customerPhoneNum = rs.getString("ContactNumber");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			Payment payment = new Payment(cart, customerName, customerAddress, customerPhoneNum);
			payment.paymentPage();
			if (payment.billPaidFlag == 1) {
				billPaidFlag = 1;
				cart = new Cart();
				cartFlag = 0;
				billPaidFlag = 0;
				checkFlag = -2;
			}
		} else {
			System.out.println("CART IS EMPTY!");
		}
	}

	private int checkExit() throws IOException, SQLException {
		if (cartFlag == 1) {
			System.out.println("YOU HAVE A PENDING CART!");
			System.out.print("DO YOU WANT TO MAKE PAYMENT (PRESS Y): ");
			String choice = sc.nextLine();
			if (choice.equalsIgnoreCase("Y")) {
				proceedPayment(cart);
				if (billPaidFlag != 1 && checkFlag == -1)
					return -1;
				else
					return 0;
			} else {
				cart.cancelCart();
				cart = new Cart();
				cartFlag = 0;
				billPaidFlag = 0;
			}
		}
		System.out.println("THANK YOU!");
		return 10;
	}

	private void updateQty(int removedQuantity, int productId) {
		try {
			int index = productIds.indexOf(productId);
			int productQuantity = productQuantities.get(index);
			productQuantities.set(index, Math.max(productQuantity - removedQuantity, 0));

			pst = con.prepareStatement("UPDATE products SET Quantity = ? WHERE productID = ?");
			pst.setInt(1, productQuantities.get(index));
			pst.setInt(2, productId);
			int m = pst.executeUpdate();
			if (m == 0)
				System.out.println("PRODUCT QUANTITY UPDATION FAILED!");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void viewProfile(int customerId) {
		String query = "SELECT * FROM customer_details WHERE custId = ?";

		try {
			pst = con.prepareStatement(query);
			pst.setInt(1, customerId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				System.out.println("Customer Profile Details:");
				System.out.println("Customer ID: " + rs.getInt("custId"));
				System.out.println("Name: " + rs.getString("Name"));
				System.out.println("Age: " + rs.getInt("Age"));
				System.out.println("Email: " + rs.getString("email"));
				System.out.println("Address: " + rs.getString("Address"));
				System.out.println("Contact Number: " + rs.getString("ContactNumber"));
			} else {
				System.out.println("No profile found for Customer ID: " + customerId);
			}
		} catch (Exception e) {
			System.out.println("Error while retrieving profile: " + e.getMessage());
		}
	}

	public void editProfile(int customerId) {

		String newPassword, newEmail, newContact;

		System.out.println("EDIT PROFILE SECTION");

		String query = "UPDATE `custinfo` SET "
				+ "`Name` = COALESCE(NULLIF(?, ''), `Name`), "
				+ "`password` = COALESCE(NULLIF(?, ''), `password`), "
				+ "`Age` = COALESCE(NULLIF(?, ''), `Age`), "
				+ "`email` = COALESCE(NULLIF(?, ''), `email`), "
				+ "`Address` = COALESCE(NULLIF(?, ''), `Address`), "
				+ "`ContactNumber` = COALESCE(NULLIF(?, ''), `ContactNumber`) "
				+ "WHERE `custId` = ?";

		try {
			pst = con.prepareStatement(query);

			String newName = validator.getStringInput("Enter new name (leave empty to keep current): ", 20);
			if (validator.choiceMaker("DO YOU WANT TO UPDATE PASSWORD (Y / N) ? ")) {
				newPassword = validator.encryptPassword(validator.setValidPassword());
			} else {
				newPassword = "";
			}
			String newAge = validator.getStringInput("Enter new age (leave empty to keep current): ", 3);
			if (validator.choiceMaker("DO YOU WANT TO UPDATE Email (Y / N) ? ")) {
				newEmail = validator.setValidEmail();
			} else {
				newEmail = "";
			}
			String newAddress = validator.getStringInput("Enter new address (leave empty to keep current): ", 20);
			if (validator.choiceMaker("DO YOU WANT TO UPDATE CONTACT NUMBER (Y / N) ? ")) {
				newContact = validator.setValidPhoneNumber();
			} else {
				newContact = "";
			}

			pst.setString(1, newName);
			pst.setString(2, newPassword);
			pst.setString(3, newAge);
			pst.setString(4, newEmail);
			pst.setString(5, newAddress);
			pst.setString(6, newContact);
			pst.setInt(7, customerId);

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

}
