package dz.nier.server.connectionClient;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.inOutThread.BasicThread;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.server.bdd.dao.ClientDAO;
import dz.nier.server.salon.ChoixSalon;
import dz.nier.server.utile.ServerOut;

public class Acceptation extends BasicThread {

	protected Acceptation(Distributteur d) {
		super(d);
		this.id = 0;
		this.out_id = 1;
		this.setName("Acceptation");
		this.start();
	}

	public void run() {
		boolean accepter = false;
		while (!accepter) {
			Requete demande = this.in();
			if (!this.isConnected())
				return;
			RequestValue type = (RequestValue) demande
					.getAttribut(RequestKeys.RAISON);
			if (type.equals(RequestValue.CONNECTION_MOMBRE))
				accepter = this.mombre(demande);
			else if (type.equals(RequestValue.CONNECTION_ANONYME))
				accepter = this.anonyme(demande);
			else if (type.equals(RequestValue.INSCRIPTION))
				accepter = this.inscription(demande);
			else
				erreurType();
		}
	}

	private boolean mombre(Requete r) {
		ClientBean client = new ClientDAO().getClientMombre(
				(String) r.getAttribut(RequestKeys.PSEUDO),
				(String) r.getAttribut(RequestKeys.PASSWD));
		if (client != null) {
			renvoiClient(client);
			return true;
		}
		return nonConnecter(new Requete(RequestKeys.ERREUR,
				"le pseudo et le mot de pass ne corespende pas"));
	}

	private boolean anonyme(Requete r) {
		if (new ClientDAO().findClientPseudo((String) r
				.getAttribut(RequestKeys.PSEUDO)) != null)
			return nonConnecter(new Requete(RequestKeys.ERREUR,
					"le pseudo entré existe déja"));
		renvoiClient(new ClientDAO().insertObj(new ClientBean((String) r
				.getAttribut(RequestKeys.PSEUDO))));
		return true;
	}

	private boolean inscription(Requete r) {
		if (new ClientDAO().findClientPseudo((String) r
				.getAttribut(RequestKeys.PSEUDO)) != null)
			return nonConnecter(new Requete(RequestKeys.ERREUR,
					"le pseudo entré existe déja"));
		ClientBean client = new ClientDAO().insertObj(new ClientBean((String) r
				.getAttribut(RequestKeys.PSEUDO), (String) r
				.getAttribut(RequestKeys.PASSWD), (String) r
				.getAttribut(RequestKeys.NOM), (String) r
				.getAttribut(RequestKeys.PRENOM)));
		renvoiClient(client);
		ServerOut.out("le client" + client.toSimpleString() + " s'est inscrie");
		return true;
	}

	private void erreurType() {
		nonConnecter(new Requete(RequestKeys.ERREUR,
				"une ereur s'est produit lors de l'envoi de la requete"));
	}

	private void renvoiClient(ClientBean client) {
		ServerOut
				.out("le client" + client.toSimpleString() + " s'est connecté");
		Requete repence = new Requete();
		ChoixSalon cs = new ChoixSalon(this.distributeur, client);
		repence.ajouterAttribut(RequestKeys.REPENCE, RequestValue.OK);
		repence.ajouterAttribut(RequestKeys.OUT_ID, cs.getId());
		repence.ajouterAttribut(RequestKeys.CLIENT, client);
		this.out(repence);
		cs.start();
	}

}
