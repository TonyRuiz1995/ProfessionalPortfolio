package mysql.second;

import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

import com.mysql.jdbc.Statement;

public class AddCustomer {
	
	private static java.sql.Statement myStatement;
	
	public AddCustomer(java.sql.Statement conn){
		myStatement = conn;
	}
	
  
//method to add to customer table
public static void Add(){
		
		Scanner input = new Scanner(System.in);
		
		boolean errors;
	    
		System.out.println("Add CustomerID");
	    String c_ID;
		c_ID = input.nextLine();
		
		//check length
		while(!checkSize(5, c_ID)){
			System.out.println("Add CustomerID");
			c_ID = input.nextLine();
		}
		
	    //check if ID is unique
			while(!CheckID(c_ID, myStatement)){
				System.out.println("ERROR, THat ID ALREADY Exist");
				System.out.println("Add CustomerID");
				c_ID = input.nextLine();
			
		}
		
		
		System.out.println("Add Company Name");
		String comp_Name;
		comp_Name = input.nextLine();
		
		System.out.println("Add Contact Name");
		String c_Name;
		c_Name = input.nextLine();
		
		System.out.println("Add Contact Title");
		String c_Title;
		c_Title = input.nextLine();
		
		System.out.println("Add Address");
		String address;
		address = input.nextLine();
		
		System.out.println("Add City");
		String city;
		city = input.nextLine();
		
		System.out.println("Add Region");
		String region;
		region = input.nextLine();
		
		System.out.println("Add Postal Code");
		String code;
		code = input.nextLine();
		
		System.out.println("Add Country");
		String country;
		country = input.nextLine();
		
		System.out.println("Add Phone");
		String phone;
		phone = input.nextLine();
		
		while(!phone.matches("^((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}")){
			System.out.println("Please enter valid phone number format (xxx-xxx-xxxx)");
			System.out.println("Add Phone Number");
			phone = input.nextLine();
		}
		
		
		System.out.println("Add Fax");
		String fax;
		fax = input.nextLine();

		while(!fax.matches("^((\\(\\d{3}\\) ?)|(\\d{3}-))?\\d{3}-\\d{4}")){
			System.out.println("Please enter valid phone number format (xxx-xxx-xxxx)");
			System.out.println("Add Fax number");
			fax = input.nextLine();
		}
		
		
			 String sql = "insert into customers "
					               + "(CustomerID, CompanyName, ContactName,ContactTitle, Address, City, Region, PostalCode, Country, Phone, Fax )"								                  
					               + "values ('" + c_ID +  "','"
					               + comp_Name +  "','"
					               + c_Name    + "','"
					               + c_Title   + "','"
					               + address   + "','"
					               + city	   + "','"
					               + region	   + "','"
					               + code	   + "','"
					               + country   + "','"	   
					               + phone	   + "','"
					               + fax   + "')";

			 
			 
			 

					    try {
							myStatement.executeUpdate(sql);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    
					    System.out.println("Insert Complete");
		
	}
  //check customerid 
  //make sure CustomerID is unique
  public static boolean CheckID(String Id, java.sql.Statement b){
	  boolean okay = true;
	  String sql = "select CustomerID from customers where CustomerID = '"+Id+"'";
	  
	  try {
		ResultSet rs = b.executeQuery(sql);
		if(rs.next()){
			System.out.println("ERROR: THAT CUSTOMER ALREADY EXISTS! PLEASE ADD A DIFFERENT CUSTOMER");
		    okay = false;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 
	  return okay;
  }
  
  //check size on customerid
  public static boolean checkSize(int len, String val){
	  
	  boolean flag = true;
	  if(val.length()>len){
		  System.out.println("Entry is too long");
		  return false;
	  }
	  return flag;
	  
  }
	

}
