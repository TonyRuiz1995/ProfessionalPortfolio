package mysql.second;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Products {

	private static java.sql.Statement myStatement;
	
	public Products(java.sql.Statement conn){
		myStatement = conn;
	}
	public static void Restock(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the ProductID that you want to order");
		String p_ID = sc.nextLine();

		//check if productid exists or not
		//if not, prompt for id again
			while(!CheckP_ID(p_ID, myStatement)){
			System.out.println("ERROR, THat ID doesn't Exist");
			System.out.println("Enter the ProductID that you want to reorder");
			p_ID  = sc.nextLine();
		
	}
			
			//check for numeric user input
		try{
			System.out.println("How many units do you want to order?");
			int units = sc.nextInt();
			
			//update unitsorder;
			UpdateProductOnOrder(p_ID,units, myStatement);
					


		}
		catch(InputMismatchException exc){
			System.out.println("This is not an integer");
		}
	
			
	}
	
 public static void UpdateProductOnOrder(String Id,int units, java.sql.Statement b){
		 
		 //increments products
		 String sql = "update products "
				 +    " set UnitsInStock = UnitsInStock+ "+ units+" "
				 +    "where ProductID = "+Id+"  ";
		 
		 try {
				int rs = b.executeUpdate(sql);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	 }
	
	 public static boolean CheckP_ID(String Id, java.sql.Statement b){
		  boolean okay = true;
		  String sql = "select ProductID from products where ProductID = '"+Id+"'";
		  
		  try {
			ResultSet rs = b.executeQuery(sql);
			if(!rs.next()){
				System.out.println("ERROR: THAT ProductID doesn't EXISTS! PLEASE ADD A DIFFERENT Product");
			    okay = false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
		  return okay;
	  }

}

