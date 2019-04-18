package dz.nier.server.bdd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import dz.nier.autre.bean.MessageBean;
import dz.nier.server.bdd.ConnectionBDD;


public class MessageDAO extends DAO<MessageBean> {

	public static final String NOM_TABLE = "message";

	public MessageDAO() {
		super(NOM_TABLE);
		insertStatement = ConnectionBDD
				.getPrepareStatement("INSERT INTO message( id_salon, id_client, message, auteur,date) VALUES( ?, ?, ?, ?,NOW())");
	}

	@Override
	protected MessageBean instance(ResultSet rs) {
		try {
			if (rs.next()) {
				return new MessageBean(rs.getLong("id"), rs.getString("message"),
						rs.getString("auteur"), rs.getLong("id_client"),
						rs.getLong("id_salon"), rs.getDate("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void insert(MessageBean obj) {
		try {
			this.insertStatement.setLong(1, obj.getId_salon());
			this.insertStatement.setLong(2, obj.getId_client());
			this.insertStatement.setString(3, obj.getText());
			this.insertStatement.setString(4, obj.getAuteur());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(MessageBean obj) {
	}
}
