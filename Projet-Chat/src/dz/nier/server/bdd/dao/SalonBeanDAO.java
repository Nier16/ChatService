package dz.nier.server.bdd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import dz.nier.autre.bean.SalonBean;
import dz.nier.server.bdd.ConnectionBDD;

public class SalonBeanDAO extends DAO<SalonBean> {

	public static final String NOM_TABLE = "salon";

	public SalonBeanDAO() {
		super(NOM_TABLE);
		this.insertStatement = ConnectionBDD
				.getPrepareStatement("INSERT INTO "
						+ NOM_TABLE
						+ "(nom,passwd,sujet,type,date,id_createur) VALUES(?,?,?,?,NOW(),?)");
		this.updateStatement = ConnectionBDD.getPrepareStatement("UPDATE "
				+ NOM_TABLE + " nom=?, passwd=?, sujet=?, type=? WHERE id=?");
	}

	protected SalonBean instance(ResultSet rs) {
		try {
			if (rs.next()) {
				return new SalonBean(rs.getLong("id"),
						new ClientDAO().select(rs.getLong("id_createur")),
						rs.getString("nom"), rs.getString("sujet"),
						SalonBean.Type.valueOf(rs.getString("type")),
						rs.getString("passwd"), rs.getDate("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void insert(SalonBean obj) {
		try {
			this.insertStatement.setString(1, obj.getNom());
			this.insertStatement.setString(2, obj.getPassword());
			this.insertStatement.setString(3, obj.getSujet());
			this.insertStatement.setString(4, obj.getType().toString());
			this.insertStatement.setLong(5, obj.getcreateur().getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(SalonBean obj) {
		try {
			this.updateStatement.setString(1, obj.getNom());
			this.updateStatement.setString(2, obj.getPassword());
			this.updateStatement.setString(3, obj.getSujet());
			this.updateStatement.setString(4, obj.getSujet());
			this.updateStatement.setLong(5, obj.getId());
			this.updateStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<SalonBean> selectAll() {
		return new ArrayList<SalonBean>(super.multiSelect(""));
	}

}
