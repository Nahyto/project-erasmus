package fr.ulille.iut;

import java.sql.*;

public class SqlRequest {

	public static void main( String args[] )
	{
		Connection c = null;
	    Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		System.out.println("Opened database successfully");
		
		try {
			stmt = c.createStatement();
			String sql = "DROP TABLE IF EXISTS Product";
			stmt.executeUpdate(sql);
			sql = "CREATE TABLE Product " +
					"(id INT PRIMARY KEY     NOT NULL," +
					" priority           TEXT    NOT NULL, " + 
					" amont            INT     NOT NULL, " + 
					" color        CHAR(50), " + 
					" description         REAL)"; 
			stmt.executeUpdate(sql);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		System.out.println("Table created successfully");
		
	    try {
	        String sql = "INSERT INTO Product (id,priority,amont,color,description) " +
	                     "VALUES (1, 1, 32, 'Blue', 'hello' );"; 
	        stmt.executeUpdate(sql);

	        sql = "INSERT INTO Product (id,priority,amont,color,description) " +
	              "VALUES (2, 2, 14, 'Black', 'Goodbye' );"; 
	        stmt.executeUpdate(sql);

	        sql = "INSERT INTO Product (id,priority,amont,color,description) " +
	              "VALUES (3, 2, 15, 'Red', 'hi' );"; 
	        stmt.executeUpdate(sql);

	        sql = "INSERT INTO Product (id,priority,amont,color,description) " +
	              "VALUES (4, 3, 5, 'White', 'Hallo' );"; 
	        stmt.executeUpdate(sql);

	        stmt.close();
	      } catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
	      }
	      System.out.println("Records created successfully");
	      
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
