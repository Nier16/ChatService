package dz.nier.autre.bean;

import java.sql.Date;

public class MessageBean extends BasicBean {
	private static final long serialVersionUID = 1L;
	private long id_client;
	private long id_salon;
	private String auteur;
	private Date date;
	private String text;

	public MessageBean(String text, String auteur, long id_client, long id_salon) {
		this.id_client = id_client;
		this.id_salon = id_salon;
		this.text = text;
		this.auteur = auteur;
	}

	public MessageBean(long id, String text, String auteur, long id_client,
			long id_salon, Date date) {
		super(id);
		this.text = text;
		this.auteur = auteur;
		this.id_client = id_client;
		this.id_salon = id_salon;
		this.date = date;
	}

	public String toString() {
		return super.toSimpleString() + "text(" + this.text + ") auteur(id : "
				+ id_client + "  |  pseudo : " + auteur + ") salon(" + id_salon
				+ ") date(" + this.date + "))";
	}

	public String toSimpleString() {
		String t = text;
		if (this.text.length() > 8) {
			t = text.substring(7);
			t += "...";
		}
		return super.toSimpleString() + "text(" + t + ") auteur(id : "
				+ id_client + "  |  pseudo : " + auteur + ") salon(" + id_salon
				+ ")";
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getId_salon() {
		return id_salon;
	}

	public void setId_salon(long id_salon) {
		this.id_salon = id_salon;
	}

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

}
