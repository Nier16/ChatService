package dz.nier.server.salon;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.inOutThread.ClientBasicThread;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;

public class Chat extends ClientBasicThread {

	private ClientBean client;
	private Salon salon;
	private boolean connect = true;

	public Chat(Distributteur d, ClientBean client, Salon salon) {
		super(d, client.getId());
		this.client = client;
		this.salon = salon;
		this.setName("ecouteur message(Server) : client(" + client.getId()
				+ ") salon(" + salon.getSalon().getId() + ")");
	}

	public void run() {
		Requete r = in();
		if (!this.connected)
			return;
		this.out_id = (long) r.getAttribut(RequestKeys.OUT_ID);
		out(new Requete(new Object[][] {
				{ RequestKeys.HISTORY, salon.getHistory() },
				{ RequestKeys.ACTIVITE, salon.getActivite() },
				{ RequestKeys.MOMBRE_EN_LIGNE,
						salon.construireChaineMombreEnLigne() } }));
		while (connect) {
			r = in();
			if (!this.connected)
				return;
			this.sendRequest(r);
		}
	}

	public void sendRequest(Requete r) {
		this.client.setNombreMessage(this.client.getNombreMessage() + 1);
		r.ajouterAttribut(RequestKeys.AUTEUR, this.client.getPseudo());
		r.ajouterAttribut(RequestKeys.RAISON, RequestValue.ENVOI_MESSAGE);
		this.salon.takeRequest(r, this.client.getId());
	}

	public void refreshInformation(String mombreEnLigne, String activite) {
		out(new Requete(new Object[][] {
				{ RequestKeys.RAISON, RequestValue.REFRESH_INFORMATION },
				{ RequestKeys.ACTIVITE, activite },
				{ RequestKeys.MOMBRE_EN_LIGNE, mombreEnLigne } }));
	}

	public void disconnect() {
		close();
		salon.removeMe(this);
	}

	public void close() {
		this.connect = false;
		super.desconnect();
	}

	public void connect() {
		this.connect = true;
	}

	public ClientBean getClient() {
		return this.client;
	}

}
