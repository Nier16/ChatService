package dz.nier.server.salon;

import java.util.ArrayList;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.MessageBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;
import dz.nier.server.bdd.dao.MessageDAO;

public class Salon {
	private ArrayList<Chat> clients = new ArrayList<Chat>();
	private SalonBean salon;
	private long lastClientID;
	private String history = new String();
	private String activite = new String();

	protected Salon(SalonBean salon) {
		this.setSalon(salon);
	}

	public Long addClient(ClientBean client, Distributteur d) {
		Chat bt = new Chat(d, client, this);
		this.salon.addClient(client);
		this.clients.add(bt);
		bt.start();
		this.refreshInformationChatClient("- " + client.getPseudo()
				+ " s'est connecté");
		return this.lastClientID = bt.getId();
	}

	public void takeRequest(Requete r, long id_client) {
		new MessageDAO().insertObj(new MessageBean((String) r
				.getAttribut(RequestKeys.MESSAGE), (String) r
				.getAttribut(RequestKeys.AUTEUR), id_client, salon.getId()));
		this.history += ((String) r.getAttribut(RequestKeys.AUTEUR) + " : "
				+ (String) r.getAttribut(RequestKeys.MESSAGE) + "\n");
		for (Chat e : this.clients)
			e.out(r);
	}

	private void refreshInformationChatClient(String activite) {
		this.activite += activite + "\n";
		String mombreEnLigne = this.construireChaineMombreEnLigne();
		for (Chat cs : this.clients)
			cs.refreshInformation(mombreEnLigne, activite);
	}

	public String construireChaineMombreEnLigne() {
		String res = "Mombre en Ligne : \n";
		int i = 1;
		for (ClientBean c : this.salon.getClients())
			res += Utile.petitTab() + i++ + "- " + c.getPseudo() + "\n";
		return res;

	}

	public void close() {
		for (Chat e : this.clients)
			e.close();
		this.clients.clear();
	}

	public SalonBean getSalon() {
		return salon;
	}

	public void setSalon(SalonBean salon) {
		this.salon = salon;
	}

	public long getLastClientID() {
		return lastClientID;
	}

	public void setLastClientID(long lastClientID) {
		this.lastClientID = lastClientID;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public void removeMe(Chat clientSalon) {
		this.clients.remove(clientSalon);
		this.refreshInformationChatClient("- "
				+ clientSalon.getClient().getPseudo() + " s'est deconnecté.");
	}

	public String getActivite() {
		return activite;
	}

	public void setActivite(String activite) {
		this.activite = activite;
	}
}
