package dz.nier.client.salon;

import javax.swing.JOptionPane;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.ClientBasicThread;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;

class ConnectionSalonThread extends ClientBasicThread {
	private SalonBean salon;
	private ClientBean client;

	public ConnectionSalonThread(Distributteur d, SalonBean salon,
			ClientBean client, Long outId) {
		super(d, client.getId());
		this.out_id = outId;
		this.salon = salon;
		this.client = client;
		this.setName("Connection Salon");
		this.start();
	}

	public void run() {
		if (!salon.getType().equals(SalonBean.Type.PRIVE) || testPasswd()) {
			out(new Requete(new Object[][] {
					{ RequestKeys.RAISON, RequestValue.CONNECTION_SALON },
					{ RequestKeys.SALON, salon },
					{ RequestKeys.OUT_ID, this.id } }));
			Requete repence = in();
			if (!this.connected)
				return;
			if (repence.getAttribut(RequestKeys.REPENCE)
					.equals(RequestValue.OK))
				new Chat(distributeur,
						(long) repence.getAttribut(RequestKeys.OUT_ID), salon,
						client);
			else
				Utile.showErrorDialog(
						(String) repence.getAttribut(RequestKeys.ERREUR),
						"Erreur");
		} else
			Utile.showErrorDialog("le motdepass est incorrect !!!", "Erreur");
	}

	private boolean testPasswd() {
		String passwd = JOptionPane.showInputDialog(null,
				"entrez le mdp de ce salon", "Passwd",
				JOptionPane.QUESTION_MESSAGE);
		return (passwd != null && passwd.equals(salon.getPassword()));
	}
}