package fr.ulille.iut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Launch {
	//URL de connexion

	//Objet Connection
	private static Connection connect;

	//Constructeur privé
	private Launch(){
		try {
			Class.forName("org.sqlite.JDBC");
			connect = DriverManager.getConnection("jdbc:sqlite:test.db");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//Méthode qui va nous retourner notre instance et la créer si elle n'existe pas
	public static Connection getInstance(){
		if(connect == null){
			new Launch();
		}
		return connect;   
	}   
}
