/*
Driver class
-Class that will run the main program
-Call functions from others classes

*/

package mysql.second;
import java.sql.*;
import java.util.Scanner;
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		try{
			
			//1.Get a connection to database
			//port:3306
			Connection myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind","root", "tony");
			
			
			//2.create a statement
			Statement myStatement = myConn.createStatement();
			
			
			boolean run = true;
			while(run){
				
				System.out.println("Please select one of the following options");
				System.out.println("Enter 1 to add a customer");
				System.out.println("Enter 2 to add an order");
				System.out.println("Enter 3 to remove an order");
				System.out.println("Enter 4 to ship an order");
				System.out.println("Enter 5 to print pending orders with customer information");
				System.out.println("Enter 6 to restock parts");
				System.out.println("Enter 7 to exit");
				int x = 0;
				
				try{
					
					x = Integer.parseInt(sc.nextLine());
					
					
					switch(x){
					
					
					case 1:{
						//adding customer
					
				    //create addCustomer object
					AddCustomer a = new AddCustomer(myStatement);
					a.Add();
						

						
				    break;
						
						
					}
					case 2:{
						
						//Scanner sc = new Scanner(System.in);
						Scanner sc1 = new Scanner(System.in);

						System.out.println("Add OrderID");
					    String o_ID;
						o_ID = sc1.nextLine();
						//need to check if the order id is repetitive, needs to be unique
						while(!CheckO_ID(o_ID, myStatement)){
							System.out.println("ERROR, THat ID ALREADY Exist");
							System.out.println("Add OrderID");
							o_ID = sc.nextLine();
						
					     }
						
						
					//add order
					AddOrder b = new AddOrder(myStatement,o_ID);
			        b.Add();
					AddOrderDetails c = new AddOrderDetails(myStatement,o_ID);
					c.Add();						
			
			   break;
					}
					case 3:{
						
						Scanner sc1 = new Scanner(System.in);

						System.out.println("Add OrderID");
					    String o_ID;
						o_ID = sc1.nextLine();
						//need to check if the order id is repetitive, needs to be unique
						while(!CheckO_ID1(o_ID, myStatement)){
							System.out.println("ERROR, THat ID ALREADY Exist");
							System.out.println("Add OrderID");
							o_ID = sc.nextLine();
						
					     }
						
						AddOrderDetails x2 = new AddOrderDetails(myStatement, o_ID);
						x2.Remove();
						
						AddOrder x1 = new AddOrder(myStatement, o_ID);
						x1.Remove();
						break;
						
					}
					case 4:{
						//ship an order
						Scanner sc1 = new Scanner(System.in);
						//enter the orderID that you want to ship out
						System.out.println("Add OrderID that you want to ship out(MUST BE IN PENDING ORDERS LIST)");
					    String o_ID;
						o_ID = sc1.nextLine();
						//need to check if the order id is repetitive, needs to be unique
						while(!CheckO_ID2(o_ID, myStatement)){
							System.out.println("ERROR, THat ID doesnt Exist");
							System.out.println("Add OrderID");
							o_ID = sc.nextLine();
						
					     }
						
						AddOrder x3 = new AddOrder(myStatement, o_ID);
						x3.ShipOrder();//ship it out
						break;
					}
					case 5:{
						//print pending parts
						AddOrder x2 = new AddOrder(myStatement);
						x2.PrintPendingOrders(myStatement);
						break;
					}
					case 6:{
						//restock
						//get product id
						//how many parts do you want to order
						//update unitsonorder
						Products p1 = new Products(myStatement);
						p1.Restock();
					
						break;
					}
					
					case 7:{
						System.out.println("Exiting");
						run = false;
						break;
						
					}
					
					}
				}
				catch(IllegalArgumentException e){
					System.out.println("ERROR");
					System.out.println("Please enter a valid integer 1-7");
				}

				
				
			}
			
			
		}
		catch (Exception exc){
			exc.printStackTrace();
		}

	}
	
	
	//check if orderID already exists in orders table
	 public static boolean CheckO_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select OrderID from orders where OrderID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(rs.next()){
				System.out.println("ERROR: THAT Order already EXISTS! PLEASE ADD A DIFFERENT OrderID");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	 
	 //check if a particular order id does not exist in a table
	 public static boolean CheckO_ID1(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select OrderID from orders where OrderID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT Order Doesnt EXISTS! PLEASE ADD A DIFFERENT OrderID");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	
	 //check if an orderid exist in the Pending orders list
	 public static boolean CheckO_ID2(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select orderid,orderdate " 
				  + "from orders where shippeddate is null and orderid = "+Id+"";		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT Order Doesnt EXISTS! PLEASE ADD A DIFFERENT OrderID");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }
	

}
