package com.E_CommercePlatform.Entities;

import com.E_CommercePlatform.CustomerApp;
import com.E_CommercePlatform.util.ArrayList;
import java.io.*;
import java.sql.*;

public class Bill {
	static Connection con = CustomerApp.con;
	private int bill_id;
	private String cust_name;
	private String bill_addr;
	private String cust_phone;
	public float total_amount;
	private ArrayList<Integer> productIds = new ArrayList<Integer>();
	private ArrayList<String> productNames = new ArrayList<String>();
	private ArrayList<Integer> productQunatities = new ArrayList<Integer>();
	private ArrayList<Float> productPrices = new ArrayList<Float>();

	Bill(String cname, String address, String phoneNum, ArrayList<Integer> arrayList, ArrayList<String> arrayList2,
			ArrayList<Integer> arrayList3, ArrayList<Float> arrayList4) throws IOException {
		cust_name = cname;
		bill_addr = address;
		cust_phone = phoneNum;
		total_amount = 0.0f;
		productIds = arrayList;
		productNames = arrayList2;
		productQunatities = arrayList3;
		productPrices = arrayList4;
	}

	private void generateBill() throws IOException, SQLException {
		bill_id = setBillId();
		float sum = 0.0f;
		for (int i = 0; i < productIds.size(); i++) {
			sum = sum + productPrices.get(i);
		}
		total_amount = sum;
	}

	public void displayBill() throws IOException, SQLException {
		generateBill();
		System.out.println("YOUR BILL IS :\n");
		System.out.println(
				"************************************************************************************************\n");
		System.out.printf("BILL ID                  =  %-5d\n", bill_id);
		System.out.printf("CUSTOMER NAME            =  %-20s\n", cust_name);
		System.out.printf("CUSTOMER CONTACT NUMBER  =  %-20s\n", cust_phone);
		System.out.printf("CUSTOMER ADDRESS         =  %-30s\n", bill_addr);
		System.out.println(
				"************************************************************************************************\n");
		System.out.printf("%-20s \t %-20s \t %-20s \t %-20s\n", "PRODUCT_ID", "PRODUCT_NAME", "QUANTITY PURCHASED",
				"TOTAL_PRICE");
		for (int i = 0; i < productIds.size(); i++) {
			System.out.printf("%-20d \t %-20s  \t %-20d \t %-20f\n", productIds.get(i), productNames.get(i),
					productQunatities.get(i),
					productPrices.get(i));
		}
		System.out.println(
				"************************************************************************************************\n");
		System.out.printf("TOTAL AMOUNT PAYABLE = Rs. " + total_amount + "\n");
		System.out.println(
				"************************************************************************************************\n");
	}

	public void addToDatabase() throws IOException {
		int x;
		try {
			PreparedStatement ps = con
					.prepareStatement("insert into bills(bill_id,cust_name,bill_addr,total_amt) values(?,?,?,?)");
			ps.setInt(1, bill_id);
			ps.setString(2, cust_name);
			ps.setString(3, bill_addr);
			ps.setFloat(4, total_amount);
			x = ps.executeUpdate();
			if (x == 0)
				System.out.println("BILL NOT ADDED TO DATABASE !");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static int setBillId() throws SQLException {
		int x = 0;

		PreparedStatement pst = con.prepareStatement("select bill_id from bill_details");
		ResultSet rs = pst.executeQuery();
		while (rs.next()) {
			x = rs.getInt(1);
		}
		return x + 1;
	}
}
