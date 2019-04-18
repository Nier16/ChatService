package dz.nier.server.bdd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import dz.nier.autre.bean.ClientBean;
import dz.nier.server.bdd.ConnectionBDD;


public class ClientDAO extends DAO<ClientBean> {

	public static final String NOM_TABLE = "client";

	public ClientDAO() {
		super(NOM_TABLE);
		insertStatement = ConnectionBDD
				.getPrepareStatement("INSERT INTO client( nom, prenom, pseudo, passwd, type, date) "
						+ " VALUES( ?, ?, ?, ?, ?, NOW())");
		updateStatement = ConnectionBDD
				.getPrepareStatement("UPDATE client "
						+ " nom=?, prenom=?, pseudo=?, passwd=?, type=? WHERE pseudo=?");
	}

	protected ClientBean instance(ResultSet rs) {
		try {
			if (rs.next()) {
				return new ClientBean(rs.getLong("id"), rs.getString("pseudo"),
						rs.getString("passwd"), rs.getString("nom"),
						rs.getString("prenom"), ClientBean.Type.valueOf(rs
								.getString("type")), rs.getDate("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void insert(ClientBean obj) {
		try {
			this.insertStatement.setString(1, obj.getNom());
			this.insertStatement.setString(2, obj.getPrenom());
			this.insertStatement.setString(3, obj.getPseudo());
			this.insertStatement.setString(4, obj.getMotDePass());
			this.insertStatement.setString(5, obj.getType().toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(ClientBean obj) {
		try {
			this.updateStatement.setString(1, obj.getNom());
			this.updateStatement.setString(2, obj.getPrenom());
			this.updateStatement.setString(3, obj.getPseudo());
			this.updateStatement.setString(4, obj.getMotDePass());
			this.updateStatement.setString(5, obj.getType().toString());
			this.updateStatement.setLong(6, obj.getId());
			this.updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ClientBean findClientPseudo(String pseudo) {
		return super.find("WHERE pseudo='" + pseudo + "'");
	}

	public ClientBean getClientMombre(String pseudo, String passwd) {
		return super.find("WHERE pseudo='" + pseudo + "' and passwd='" + passwd
				+ "'");
	}
}
