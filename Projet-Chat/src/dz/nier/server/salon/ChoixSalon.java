package dz.nier.server.salon;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.ClientBasicThread;
import dz.nier.autre.inOutThread.ClientBasicThreadCont;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.server.utile.ConnectionConfirmation;

public class ChoixSalon extends ClientBasicThread {

	private ClientBean client;
	private RefreshListSalonThread refreshSalonListThread;

	public ChoixSalon(Distributteur d, ClientBean client) {
		super(d, client.getId());
		new ConnectionConfirmation(d, client);
		this.client = client;
		this.setName("choixSalonServer");
		HostSalon.getInstance().addClient(client);
	}

	public void run() {
		refreshSalonListThread = new RefreshListSalonThread(distributeur,
				(long) this.in().getAttribut(RequestKeys.OUT_ID));
		while (true) {
			Requete demande = in();
			if (!this.connected)
				return;
			RequestValue raison = (RequestValue) demande
					.getAttribut(RequestKeys.RAISON);
			if (raison.equals(RequestValue.REFRESH_SALON_LIST))
				refreshSalonListThread.refresh();
			else if (raison.equals(RequestValue.CREATION_SALON))
				this.creationSalon(demande);
			else if (raison.equals(RequestValue.CONNECTION_SALON))
				this.connectionSalon(demande);
			else if (raison.equals(RequestValue.SUPPRIMER_SALON)) {
				HostSalon.getInstance().deleteSalon(
						(SalonBean) demande.getAttribut(RequestKeys.SALON));
				refreshSalonListThread.refresh();
			} else if (raison.equals(RequestValue.DESCONECT))
				this.desconnectClient();
			else
				super.nonConnecter(demande);
		}
	}

	public void desconnectClient() {
		ClientBasicThreadCont.getInstance().desconnectClient(client.getId());
	}

	private void connectionSalon(Requete r) {
		this.out_id = (long) r.getAttribut(RequestKeys.OUT_ID);
		if (HostSalon.getInstance().findSalon(
				(SalonBean) r.getAttribut(RequestKeys.SALON))) {
			out(new Requete(new RequestKeys[] { RequestKeys.REPENCE,
					RequestKeys.OUT_ID }, new Object[] {
					RequestValue.OK,
					HostSalon.getInstance().addClientToSalon(client,
							(SalonBean) r.getAttribut(RequestKeys.SALON),
							distributeur) }));
		} else
			out(new Requete(new RequestKeys[] { RequestKeys.REPENCE,
					RequestKeys.ERREUR }, new Object[] { RequestValue.ERREUR,
					"le salon que vous avez demandé a été suprimé." }));
	}

	private void creationSalon(Requete r) {
		this.out_id = (long) r.getAttribut(RequestKeys.OUT_ID);
		Salon s = HostSalon.getInstance().addSalon(
				(SalonBean) r.getAttribut(RequestKeys.SALON));
		out(new Requete(new Object[][] {
				{ RequestKeys.REPENCE, RequestValue.OK },
				{ RequestKeys.OUT_ID, s.getLastClientID() },
				{ RequestKeys.SALON, s.getSalon() } }));
		refreshSalonListThread.refresh();
	}

	public void desconnect() {
		refreshSalonListThread.desconnect();
		super.desconnect();
	}

	class RefreshListSalonThread extends ClientBasicThread {
		public static final long REFRESH_INTERVAL = 20000;
		private boolean actif = true;

		public RefreshListSalonThread(Distributteur d, long outId) {
			super(d, client.getId());
			this.out_id = outId;
			this.setName("Server Refresh Salon list");
			start();
			refresh();
		}

		public void run() {
			while (actif) {
				try {
					Thread.sleep(REFRESH_INTERVAL);
					if (!actif)
						return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				refresh();
			}
		}

		public void finish() {
			this.actif = false;
		}

		public void refresh() {
			out(HostSalon.getInstance().getAllSalon());
		}

		public boolean isActif() {
			return actif;
		}

		public void setActif(boolean actif) {
			this.actif = actif;
		}
	}
}
