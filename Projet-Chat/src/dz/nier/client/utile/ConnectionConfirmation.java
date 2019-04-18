package dz.nier.client.utile;

import dz.nier.autre.inOutThread.ClientBasicThreadCont;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;

public class ConnectionConfirmation extends dz.nier.autre.utile.ConnectionConfirmation {

	private boolean connected = true;
	public static String type = "client";

	public ConnectionConfirmation(Distributteur d, Long client_id) {
		super(d, type, client_id);
	}

	public void run() {
		Requete r = new Requete();
		while (connected) {
			try {
				Thread.sleep(Utile.CONFIRMATION_DELAY);
				this.out(r);
				Thread.sleep(Utile.ERROR_DELAY * 2);
				if (!this.distributeur.haveRequestForMe(id))
					desconnected();
				else
					this.in();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void desconnected() {
		Utile.showErrorDialog(
				"le server ne repend plus, verifié votre connection.",
				"Déconnection");
		this.desconnect();
		ClientBasicThreadCont.getInstance().desconnectClient(client_Id);
	}
}
