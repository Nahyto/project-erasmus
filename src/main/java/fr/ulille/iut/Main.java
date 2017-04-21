package fr.ulille.iut;

import java.io.IOException;

import java.net.URI;
import java.sql.Connection;
import java.sql.Statement;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main class.
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://localhost:8080/myapp/";

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		// create a resource config that scans for JAX-RS resources and providers
		// in fr.ulille.iut package
		final ResourceConfig rc = new ResourceConfig().packages("fr.ulille.iut");

		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
	}

	private static void createTable(){
		Connection c = Launch.getInstance();
		try {
			Statement stmt = c.createStatement();
			String sql = "CREATE TABLE Product " +
					"(id INTEGER PRIMARY KEY," +
					" priority          INT    NOT NULL, " + 
					" amont            INT     NOT NULL, " + 
					" color        CHAR(50), " + 
					" description         VARCHAR(50))"; 
			stmt.executeUpdate(sql);
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}finally {
				try {
					if (c != null)
						c.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
		}
	}

	/**
	 * Main method.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {
		final HttpServer server = startServer();
		createTable();
		Application.start();
		System.out.println(String.format("Jersey app started with WADL available at "
				+ "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			server.shutdownNow();
		}
	}
}

