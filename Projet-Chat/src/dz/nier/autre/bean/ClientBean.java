package dz.nier.autre.bean;

import java.sql.Date;

public class ClientBean extends BasicBean {
	private static final long serialVersionUID = 1L;

	public enum Type {
		ANONYME, MOMBRE;
	}

	private String nom;
	private String prenom;
	private String pseudo;
	private String motDePass;
	private Type type;
	private int nombreMessage = 0;
	private Date date;

	public ClientBean(String pseudo) {
		this.pseudo = pseudo;
		this.type = Type.ANONYME;
	}

	public ClientBean(String pseduo, String pass, String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
		this.pseudo = pseduo;
		this.setMotDePass(pass);
		this.type = Type.MOMBRE;
	}

	public ClientBean(long id, String pseduo, String pass, String nom,
			String prenom, Type type, Date date) {
		super(id);
		this.nom = nom;
		this.prenom = prenom;
		this.pseudo = pseduo;
		this.setMotDePass(pass);
		this.type = type;
		this.date = date;
	}

	public String toString() {
		return "(id : " + id + "  |  pseudo : " + pseudo + "  |  passwd : "
				+ motDePass + "  |  nom : " + nom + "  |  prenom : " + prenom
				+ "  |  type : " + type + ")";
	}

	public String toSimpleString() {
		return super.toSimpleString() + " pseudo : " + this.pseudo
				+ "  |  type : " + this.type + ")";
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getNombreMessage() {
		return nombreMessage;
	}

	public void setNombreMessage(int nombreMessage) {
		this.nombreMessage = nombreMessage;
	}

	public String getMotDePass() {
		return motDePass;
	}

	public void setMotDePass(String motDePass) {
		this.motDePass = motDePass;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
