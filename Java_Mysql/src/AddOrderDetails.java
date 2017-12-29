/*
Class to handle changes from Orders table to OrderDetails table
*/
package mysql.second;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddOrderDetails {
	
	
	private static java.sql.Statement myStatement;
	public static String order_id;
	
	
	public AddOrderDetails(java.sql.Statement conn, String o_id){
		myStatement = conn;
		order_id = o_id;
	}
	
	public static void Remove(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter the OrderID that you want to remove");
		String ID = sc.nextLine();
		
		
		String sql = "Delete From order_details" 
				+    " where OrderID = '"+ ID+ "'";
		
		
		
		try {
			myStatement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void Add(){
		
		Scanner sc = new Scanner(System.in);
		boolean keepAddingOrders = true;
		
        while(keepAddingOrders){
		System.out.println("Add ID");
	    String iD;
		iD = sc.nextLine();
		while(!Check_ID(iD, myStatement)){
			System.out.println("Add ID");
			iD = sc.nextLine();
		}
		
		
		System.out.println("Add ProductID");
		String p_ID;//need to check foreign key
		p_ID = sc.nextLine();
		
		//need to check if productid exist in product table
		while(!CheckP_ID(p_ID, myStatement)){
			System.out.println("ERROR, THat ID doesnt Exist");
			System.out.println("Add ProductID");
			p_ID = sc.nextLine();
		
	    }
		while(checkIfDiscontinued(p_ID, myStatement)){
			System.out.println("ERROR, THAT PRODUCT HAS BEEN DISCONTINUED, PLEASE ENTER A DIFFERENT PRODUCTID");
			System.out.println("Add ProductID");
			p_ID = sc.nextLine();
		
			
		}
		System.out.println("Add UnitPrice");
		String unit_price;
		unit_price = sc.nextLine();
		while (!isNumeric(unit_price)){
			System.out.println("This input must be numeric (double)");
			System.out.println("Add UnitPrice");
			unit_price = sc.nextLine();
		}
		
		System.out.println("Add Quantity");
		String quant;
		quant = sc.nextLine();
		while (!isINT(quant)){
			System.out.println("This input must be numeric (int)");
			System.out.println("Add Freight");
			quant = sc.nextLine();
		}
		System.out.println("Add Discount");
		String discount;
		discount = sc.nextLine();
		
		 String sql = "insert into order_details"
	               + "(ID, OrderID, ProductID,UnitPrice, Quantity,Discount)"							                  
	               + "values ('"  
	               + iD +  "','"
	               + order_id    + "','"
	               + p_ID   + "','"
	               + unit_price   + "','"
	               + quant	   + "','"
	               + discount  + "')";
	               
	               
		 System.out.println(sql);

		   try {
				myStatement.executeUpdate(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  // int q = Integer.parseInt(quant);
		   //update the products table with new order
		   UpdateProductOnOrder(quant,p_ID, myStatement);
		 
		   System.out.println("Product has successfully been added to order, would you like to add another? [Y/N]");
		   String input = sc.nextLine();
		   while(!input.matches("^[YN]$")){
			   System.out.println("Please enter a yes or no in the form of Y or N");
			   input = sc.nextLine();
		   }
		  
		   switch(input){
		   case "Y" :keepAddingOrders = true;
		   case "N" :keepAddingOrders = false;
		   }
        }
		   
	}
	
	public static boolean checkIfDiscontinued(String p_id,java.sql.Statement b){
		
		boolean disc = false;//is it discontinued
		String sql = "Select Discontinued from products" +
		             " Where ProductID = "+p_id+"";
		try {
			ResultSet rs = b.executeQuery(sql);
			if(rs.next()){
				String x = rs.getString("Discontinued");
				if(x.equals("y")){
					 disc = true;//it is discontinued
				}
				System.out.println(disc);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return disc;
		
		
	}
	
	//check if the string is of a valid int form
	private static boolean isINT(String s) {
		try  
		  {  
		    int i = Integer.parseInt(s);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;  
	}

		//check if an input is numeric
		 private static boolean isNumeric(String s) {
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
	
	
	 public static boolean CheckO_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select OrderID from orders where OrderID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(rs.next()){
				System.out.println("ERROR: THAT Order doesn't EXISTS! PLEASE ADD A DIFFERENT Order");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	
	
	
	 public static boolean CheckP_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select ProductID from products where ProductID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT Product doesn't EXISTS! PLEASE ADD A DIFFERENT CUSTOMER");
			    okay = false;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	 
	//add to units on order
	 public static void UpdateProductOnOrder(String quantity ,String Id, java.sql.Statement b){
		 System.out.println("-----------");
		 //increments products
		 String sql = "update products"
				 +    " set UnitsOnOrder = UnitsOnOrder +"+quantity+" "
				 +    "where productID = "+Id+"";
		 System.out.println(sql);
		 try {
				 b.executeUpdate(sql);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 }
/*
	 //subtract one from units on order
   public static void SubtractProductOnOrder(String Id, java.sql.Statement b){
		 
		 //increments products
		 String sql = "update products"
				 +    "set UnitsOnOrder = UnitsOnOrder-1 "
				 +    "where ProductID = '"+Id+"'  ";
		 
		 try {
				ResultSet rs = b.executeQuery(sql);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 }
  */ 
   public static boolean Check_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select ID from order_details where ID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(rs.next()){
				System.out.println("ERROR: THAT ID already EXISTS! PLEASE ADD A DIFFERENT ID");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
}
