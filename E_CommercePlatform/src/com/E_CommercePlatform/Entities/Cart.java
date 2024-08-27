package com.E_CommercePlatform.Entities;

import com.E_CommercePlatform.CustomerApp;
import com.E_CommercePlatform.util.ArrayList;

import java.io.*;
import java.sql.*;

public class Cart {
	
	Connection con = CustomerApp.con;
	PreparedStatement pst;

	private ArrayList<Integer> cartProductIds = new ArrayList<Integer>();
	private ArrayList<String> cartProductNames = new ArrayList<String>();
	private ArrayList<String> cartProductTypes = new ArrayList<String>();
	private ArrayList<Integer> cartProductQuantities = new ArrayList<Integer>();
	private ArrayList<Float> cartQuantityPrices = new ArrayList<Float>();

	public ArrayList<Integer> getpid() throws IOException {
		return cartProductIds;
	}

	public ArrayList<String> getpname() throws IOException {
		return cartProductNames;
	}

	public ArrayList<Integer> getpqty() throws IOException {
		return cartProductQuantities;
	}

	public ArrayList<Float> getprice() throws IOException {
		return cartQuantityPrices;
	}

	public void addToCart(int p_id, String p_name, String p_type, int q_pur, float q_price) throws IOException {
		cartProductIds.add(p_id);
		cartProductNames.add(p_name);
		cartProductTypes.add(p_type);
		cartProductQuantities.add(q_pur);
		cartQuantityPrices.add(q_price);
	}

	public void viewCart() throws IOException {
		int numOfProducts = cartProductIds.size();
		if (numOfProducts != 0) {
			System.out.println("YOUR CART IS : \n");
			int i;
			System.out.println(
					"***********************************************************************************************************************\n");
			System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID", "Product_Name",
					"Product_Type", "Quantity_Purchased", "Total_Price");
			System.out.println(
					"***********************************************************************************************************************\n");
			for (i = 0; i < numOfProducts; i++) {
				System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20f\n", cartProductIds.get(i), cartProductNames.get(i), cartProductTypes.get(i),
						cartProductQuantities.get(i), cartQuantityPrices.get(i));
			}
			System.out.println(
					"***********************************************************************************************************************\n");
		} else
			System.out.println("CART IS EMPTY !");

	}

	public void removeFromCart(int productId) throws IOException {
		int removeIndex;
		int x = -1;
		int previousQuantity = 0;
		int newQuantity = 0;
		removeIndex = cartProductIds.indexOf(productId);
		if (removeIndex == -1)
			System.out.println("YOU HAVE NOT ADDED THIS PRODUCT TO CART !");
		else {
			cartProductIds.remove(removeIndex);
			cartProductNames.remove(removeIndex);
			cartProductTypes.remove(removeIndex);
			cartQuantityPrices.remove(removeIndex);
			try {
				pst = con.prepareStatement("select Quantity from products where productID=?");
				pst.setString(1, Integer.toString(productId));
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					previousQuantity = Integer.parseInt(rs.getString(1));
				}
				newQuantity = previousQuantity + cartProductQuantities.get(removeIndex);
				cartProductQuantities.remove(removeIndex);
				PreparedStatement ps2 = con.prepareStatement("update products set Quantity=? where productID=?");
				ps2.setString(1, Integer.toString(newQuantity));
				ps2.setString(2, Integer.toString(productId));
				x = ps2.executeUpdate();
			} catch (Exception e) {
				System.out.println(e);
			}
			if (x != 0)
				System.out.println("CART UPDATED SUCCESSFULLY !");
		}
	}

	public void cancelCart() throws IOException {
		try {
			int prevq = 0;
			int newq = 0;
			PreparedStatement ps = con.prepareStatement("update products set Quantity=? where productId=?");
			int x;
			int y;
			for (x = 0; x < cartProductIds.size(); x++) {
				PreparedStatement ps1 = con.prepareStatement("select Quantity from products where productID=?");
				ps1.setString(1, Integer.toString(cartProductIds.get(x)));
				ResultSet rs = ps1.executeQuery();
				while (rs.next()) {
					prevq = Integer.parseInt(rs.getString(1));
				}
				newq = prevq + cartProductQuantities.get(x);
				ps.setString(1, Integer.toString(newq));
				ps.setString(2, Integer.toString(cartProductIds.get(x)));
				y = ps.executeUpdate();
				if (y == 0)
					System.out.println("PRODUCT NOT UPDATED BACK TO PRODUCTS TABLE !");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
