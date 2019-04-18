package dz.nier.server.bdd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import dz.nier.autre.bean.BasicBean;
import dz.nier.server.bdd.ConnectionBDD;

public abstract class DAO<T extends BasicBean> {

	protected PreparedStatement selectStatement;
	protected PreparedStatement insertStatement;
	protected PreparedStatement updateStatement;
	protected PreparedStatement deleteStatement;
	protected PreparedStatement lastInsertStatment;
	protected String nomTable;

	public DAO(String nomTable) {
		selectStatement = ConnectionBDD.getPrepareStatement("Select * From "
				+ nomTable + " Where id=?");
		deleteStatement = ConnectionBDD.getPrepareStatement("Delete From "
				+ nomTable + " Where id=?");
		lastInsertStatment = ConnectionBDD.getPrepareStatement("Select * FROM "
				+ nomTable + " ORDER BY id DESC LIMIT 1");
		this.nomTable = nomTable;
	}

	public T select(long id) {
		try {
			selectStatement.setLong(1, id);
			return this.instance(selectStatement.executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public T insertObj(T obj) {
		this.insert(obj);
		try {
			this.insertStatement.executeUpdate();
			return this.dernierInsertion();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(long id) {
		try {
			deleteStatement.setLong(1, id);
			deleteStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public LinkedList<T> multiSelect(String condition) {
		ResultSet rs = ConnectionBDD.execPrepareQuery("Select * from "
				+ this.nomTable + " " + condition);
		T element;
		LinkedList<T> res = new LinkedList<T>();
		while ((element = this.instance(rs)) != null)
			res.add(element);
		return res;
	}

	public T find(String condition) {
		LinkedList<T> ll = multiSelect(condition);
		if (ll.isEmpty())
			return null;
		return ll.getFirst();
	}

	public void multiInsert(LinkedList<T> objs) {
		for (T e : objs)
			this.insert(e);
	}

	public void multiDelete(LinkedList<T> objs) {
		for (T e : objs)
			this.delete(e.getId());
	}

	public void multiUpdate(LinkedList<T> objs) {
		for (T e : objs)
			this.update(e);
	}

	public T dernierInsertion() throws SQLException {
		return this.instance(this.lastInsertStatment.executeQuery());
	}

	abstract protected T instance(ResultSet rs);

	abstract protected void insert(T obj);

	abstract public void update(T obj);

}
