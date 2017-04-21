package fr.ulille.iut;

import java.sql.*;

public class Launch {
	//URL de connexion

	//Objet Connection
	private static Connection connect;

	//Constructeur privé
	private Launch(){
		try {
			Class.forName("org.sqlite.JDBC");
			connect = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//Méthode qui va nous retourner notre instance et la créer si elle n'existe pas
	public static Connection getInstance(){
		try {
			if(connect == null || connect.isClosed())
				new Launch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connect;   
	}   
}
