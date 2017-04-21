package fr.ulille.iut;

import java.sql.*;

public class SqlRequest {

	private static Statement stmt;

	public static void main( String args[] )
	{
		Connection c = null;
	    stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		System.out.println("Opened database successfully");
	      try {
	          ResultSet rs = stmt.executeQuery( "SELECT * FROM Product;" );
	          while ( rs.next() ) {
	             int id = rs.getInt("id");
	             int  priority = rs.getInt("priority");
	             int amont  = rs.getInt("amont");
	             String  color = rs.getString("color");
	             String description = rs.getString("description");
	             System.out.println( "id = " + id );
	             System.out.println( "priority = " + priority );
	             System.out.println( "amont = " + amont );
	             System.out.println( "color = " + color );
	             System.out.println( "description = " + description );
	             System.out.println();
	          }
	          rs.close();
	          stmt.close();
	          c.close();
	        } catch ( Exception e ) {
	          System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	          System.exit(0);
	        }
	        System.out.println("Operation done successfully");
	}
}
