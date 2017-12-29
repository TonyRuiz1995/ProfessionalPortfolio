package mysql.second;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class AddOrder {
	
private static java.sql.Statement myStatement;
public static String order_id;
	
	public AddOrder(java.sql.Statement conn){
		myStatement = conn;
	}
	public AddOrder(java.sql.Statement conn, String o_id){
		myStatement = conn;
		order_id = o_id;
		
	}
	//delete from orders by id
	public static void Remove(){
	
		
		String sql = "Delete From orders" 
				+    " where OrderID = '"+order_id+ "'";
		
		try {
			myStatement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//add order
	public static void Add(){
		
		Scanner sc = new Scanner(System.in);

		System.out.println("Add EmployeeID"); 
		String e_ID;//need to check foreign key in employee
		e_ID = sc.nextLine();
		
		//check if the employee id exist in employees
		while(!CheckE_ID(e_ID, myStatement)){
			System.out.println("ERROR, THat ID doesn't Exist");
			System.out.println("Add EmployeeID");
			e_ID = sc.nextLine();
		
	}
		
		
		System.out.println("Add CustomerID");
		//need to check foreign key
		String c_ID;
		c_ID = sc.nextLine();
		
		//need to check if this exists in customers table
		while(!CheckC_ID(c_ID, myStatement)){
			System.out.println("ERROR, THat ID doesn't Exist");
			System.out.println("Add CustomerID");
			c_ID = sc.nextLine();
		
	}
		
		System.out.println("Add OrderDate");
		String o_date;
		o_date = sc.nextLine();
		//using regex to match proper input
		while(!o_date.matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)")){
			System.out.println("Please enter valid date time format (YYYY-mm-dd HH:MM-ss)");
			System.out.println("Add OrderDate");
			o_date = sc.nextLine();
		}
		
		
		
		System.out.println("Add RequiredDate");
		String r_date;
		r_date = sc.nextLine();
		//using regex to match proper input
		while(!r_date.matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)")){
			System.out.println("Please enter valid date time format (YYYY-mm-dd HH:MM-ss)");
			System.out.println("Add RequiredDate");
			r_date = sc.nextLine();
		}
		
		
		System.out.println("Add ShippedDate");
		String s_date;
		s_date = sc.nextLine();
		while(!s_date.matches("^((19|20)\\d\\d)-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])\\s(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)")){
			System.out.println("Please enter valid date time format (YYYY-mm-dd HH:MM-ss)");
			System.out.println("Add ShippedDate");
			s_date = sc.nextLine();
		}
		
		
		System.out.println("Add ShipVia");
		String ship_via;//check foreign key
		ship_via = sc.nextLine();
		
		while(!CheckS_ID(ship_via, myStatement)){
			System.out.println("ERROR, THat ID doesn't Exist");
			System.out.println("Add ShipVia");
			ship_via  = sc.nextLine();
		
	}
		
		System.out.println("Add Freight");
		String freight;
		freight = sc.nextLine();
		while (!isNumeric(freight)){
			System.out.println("This input must be numeric (double)");
			System.out.println("Add Freight");
			freight = sc.nextLine();
		}
		
		System.out.println("Add ShipName");
		String ship_name;
		ship_name = sc.nextLine();
		
		System.out.println("Add ShipAddress");
		String ship_address;
		ship_address = sc.nextLine();
		
		System.out.println("Add ShipCity");
		String ship_city;
		ship_city = sc.nextLine();
		
		System.out.println("Add ShipRegion");
		String ship_region;
		ship_region = sc.nextLine();
		
		System.out.println("Add ShipPostalCode");
		String ship_pc;
		ship_pc = sc.nextLine();
		
	
		System.out.println("Add ShipCountry");
		String ship_country;
		ship_country = sc.nextLine();
	
		 String sql = "insert into orders "
				 + "(OrderID, CustomerID, EmployeeID,OrderDate, RequiredDate ,"
	               + "ShippedDate, ShipVia, Freight, ShipName, ShipAddress, ShipCity,"
	               + "ShipRegion, ShipPostalCode, ShipCountry)"	
				 
	               
	               
	               + "values ('" + order_id +  "','"
	               + c_ID +  "','"
	               + e_ID    + "','"
	               
	               + o_date   + "','"
	               + r_date   + "','"
	               + s_date	   + "','"
	               
	               
	               + ship_via	   + "','"
	               + freight	   + "','"
	               
	               
	               + ship_name   + "','"	   
	               + ship_address	   + "','"
	               + ship_city   + "','"
	               
	               + ship_region   + "','"	   
	               + ship_pc	   + "','"
	               + ship_country  + "')";
		 
		 System.out.println(sql);
		 
		 

		   try {
				myStatement.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
	    System.out.println("Insert Complete");
	}
	
	//check if an input is numeric
	 public static boolean isNumeric(String s) {
		 try  
		  {  
		    double d = Double.parseDouble(s);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;  
	}
	
	//check if customer id exists in customers
	public static boolean CheckC_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select CustomerID from customers where CustomerID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT CUSTOMER doesn't EXISTS! PLEASE ADD A DIFFERENT CUSTOMERID");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	 
	 
	 
	  //check if employee id exist is in employee table 
	 public static boolean CheckE_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select EmployeeID from employees where EmployeeID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT Employee doesn't EXISTS! PLEASE ADD A DIFFERENT Employee");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	  //check if shipping id exist in shipping table
	 public static boolean CheckS_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select ShipperID from shippers where ShipperID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT ShipperID doesn't EXISTS! PLEASE ADD A DIFFERENT Shipper");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	 
	
	 
	 public static void ShipOrder(){
		 Scanner sc = new Scanner(System.in);
		 //enter ship date
		 System.out.println("Add new ShippedDate");
		 String s_date;
		 s_date = sc.nextLine();
		 //update order with new shipdate
		 String sql = "Update orders " +
		              "set shippeddate = '" +s_date+"'"  
		              + "where orderid = '"+order_id+"'";
		 try {
				myStatement.executeUpdate(sql);
			}
		   catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 //need to grab all products with the same order order id
		 String query =  "select * from order_details where orderID ='"+order_id+"'";
		 try{
			 ResultSet rs = myStatement.executeQuery(query);
			 while(rs.next()){
				 String Product = rs.getString("ProductID");
				 int quant = rs.getInt("Quantity");
				 
				 String sql2 = "select * from products where productID = '"+Product+"'";
				 try{
				 ResultSet rs2 = myStatement.executeQuery(sql2);
				 	while(rs2.next()){
				 		int units_o = rs2.getInt("UnitsOnOrder");
				 		int units_s = rs2.getInt("UnitsInStock");
				 		if(units_s + units_o <= quant){
				 			//there's not enough parts in order or in stock and you need to order more
				 			int rem = quant - (units_s+units_o);
				 			System.out.println("You are missing "+ rem +" parts in order to complete your order for ProductID :"+Product);
				 			System.out.println("----RESTOCKING PARTS------");
				 			Products p1 = new Products(myStatement);
				 			p1.Restock();
				 		}
				 		
				 		//subtract from UnitsOnOrder 
				 		SubtractFromUnitsOnOrder(Product,units_o,Product,quant);
				 		//finish shipping order
				 	}
				 	
				 }
				 
				 catch(Exception e){
					 System.out.println("............................");
				 }

			 }
				 				 
		 }//end first try
		 
		 catch(Exception e){
			 System.out.println("ooooo");
		 }
		 
		 
	 }//end main
	
	 
	 private static void SubtractFromUnitsOnOrder(String ID,int orders,String id,int quant) {
		 
	 		System.out.println("Got here-----------");
	 		
	 		if (orders < quant){
		 		//you need to transfer parts from units in stock
		 		System.out.println("You dont have enough parts on order, transferring remaining unis from stock");
		 		//transfer (units - quant)--method
		 		int remainder = quant-orders;
		 		System.out.println("---TRANSFERRING " +remainder+ " parts from stock");
		 		TransferFromStock(ID,remainder );
		 		}

		// TODO Auto-generated method stub
		 String sql = "update products "
				 +    " set UnitsOnOrder = UnitsOnOrder -"+quant+" "
				 +    "where ProductID = "+id+"  ";
		 
		 try {
				int rs = myStatement.executeUpdate(sql);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 System.out.println("Your order has been shipped");

		
	}
	public static void TransferFromStock(String id,int rem){
		 //subtract from units on stock
		 String sql = "update products "
				 +    "set UnitsInStock = UnitsInStock - "+rem+" "
				 +    "where ProductID = "+id+"  ";
		 
		 try {
				int rs = myStatement.executeUpdate(sql);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 System.out.println("---------");
		 //add those parts to units in order
		 String sql2 = "update products "
				 +    "set UnitsOnOrder = UnitsOnOrder + "+ rem+" "
				 +    "where ProductID = "+id+"  ";
		 
		 try {
				int rs = myStatement.executeUpdate(sql2);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 System.out.println(rem +" parts should be transferred intp unitsinorder");

	 }
	 
	 public static void PrintPendingOrders(java.sql.Statement b){
		 
		 String sql = "select orderid,orderdate " 
		  + "from orders where shippeddate is null order by orderdate asc";
		 try {
				ResultSet rs = b.executeQuery(sql);
				while(rs.next()){
					String orderID = rs.getString("orderid");
					Date o_date = rs.getDate("orderdate");
					System.out.println(orderID + ":" + o_date);
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		 
	 }
	 
	

}
