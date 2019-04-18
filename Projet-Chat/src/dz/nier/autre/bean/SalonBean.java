package dz.nier.autre.bean;

import java.sql.Date;
import java.util.ArrayList;

public class SalonBean extends BasicBean {
	private static final long serialVersionUID = 1L;

	public enum Type {
		PUBLIC, PRIVE, MOMBRE;
	}

	private ClientBean createur;
	private String nom;
	private String password;
	private String sujet = "N/A";
	private Type type;
	private Date dateCreation;
	private ArrayList<ClientBean> clients = new ArrayList<ClientBean>();

	public SalonBean(long id, ClientBean createur, String nom, String sujet,
			Type type, String passwd, Date date) {
		super(id);
		this.createur = createur;
		this.nom = nom;
		this.sujet = sujet;
		this.type = type;
		this.password = passwd;
		this.setDateCreation(date);
	}

	public SalonBean(ClientBean createur, String nom, String sujet, Type type,
			String passwd) {
		this.createur = createur;
		this.nom = nom;
		this.sujet = sujet;
		this.type = type;
		this.password = passwd;
	}

	public SalonBean(ClientBean client) {
		this.nom = client.getPseudo();
		this.type = Type.MOMBRE;
		this.createur = client;
		this.addClient(client);
	}

	public String toSimpleString() {
		return (super.toString() + "nom : " + nom + "  |  Auteur : "
				+ createur.getPseudo() + " | type : " + type + ")");
	}

	public void addClient(ClientBean client) {
		if (this.clients.contains(client))
			return;
		this.clients.add(client);
	}

	public void removeClient(ClientBean client) {
		if (!this.clients.contains(client))
			return;
		this.clients.remove(client);
	}

	public ArrayList<ClientBean> getClients() {
		return clients;
	}

	public void setClients(ArrayList<ClientBean> clients) {
		this.clients = clients;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNombreMombre() {
		return this.clients.size();
	}

	public ClientBean getcreateur() {
		return createur;
	}

	public void setcreateur(ClientBean createur) {
		this.createur = createur;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String toString() {
		String res = new String("Nom : " + this.getNom() + "\nSujet : "
				+ this.getSujet() + "\nType : " + this.type + "\nCreateur : "
				+ this.createur.getPseudo());
		if (clients.isEmpty()) {
			res += "\nle salon est vide";
			return res;
		}
		res += "\nmombres connecter : ";
		for (ClientBean c : this.clients)
			res += "\n\t" + c.getPseudo();
		return res;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
}
