package dz.nier.server.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dz.nier.autre.utile.Utile;
import dz.nier.server.utile.ServerOut;

public abstract class ConnectionBDD {

	private volatile static Connection single;
	private static String url = "jdbc:postgresql://localhost:5432/Chat";
	private static String user = "postgres";
	private static String passwd = "nier";

	private ConnectionBDD() {

	}

	public static Connection getInstance() {
		if (single == null) {
			synchronized (ConnectionBDD.class) {
				try {
					single = DriverManager.getConnection(url, user, passwd);
				} catch (SQLException e) {
					e.printStackTrace();
					Utile.showErrorDialog("impossible d'ouvrire la BDD",
							"erreur");
				}
			}
		}
		return single;
	}

	public static ResultSet execPrepareQuery(String query) {
		try {
			return ConnectionBDD.getPrepareStatement(query).executeQuery();
		} catch (SQLException e) {
			ServerOut.out(e.getMessage());
		}
		return null;
	}

	public static PreparedStatement getPrepareStatement(String query) {
		try {
			return ConnectionBDD.getInstance().prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			ServerOut.out(e.getMessage());
			System.exit(0);
		}
		return null;
	}
}
