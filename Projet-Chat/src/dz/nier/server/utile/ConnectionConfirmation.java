package dz.nier.server.utile;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;
import dz.nier.server.bdd.dao.ClientDAO;

public class ConnectionConfirmation extends dz.nier.autre.utile.ConnectionConfirmation {

	public static final String TYPE = "Server";
	private ClientBean client;

	public ConnectionConfirmation(Distributteur d, ClientBean client) {
		super(d, TYPE, client.getId());
		this.client = client;
	}

	public void run() {
		Requete r = new Requete();
		while (true) {
			try {
				this.sleep(Utile.CONFIRMATION_DELAY + Utile.ERROR_DELAY);
				if (!this.distributeur.haveRequestForMe(id)) {
					this.desconnected();
					return;
				}
				in();
				out(r);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void desconnected() {
		ServerOut.out("le clien" + client.toSimpleString()
				+ " s'est déconnecter, en raison d'une perte de connection.");
		if (client.getType().equals(ClientBean.Type.ANONYME))
			new ClientDAO().delete(client_Id);
		else
			new ClientDAO().update(client);
		this.desconnect();
	}
}
