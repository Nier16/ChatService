package dz.nier.client.salon;

import dz.nier.autre.bean.ClientBean;
import dz.nier.autre.bean.SalonBean;
import dz.nier.autre.inOutThread.BasicThread;
import dz.nier.autre.inOutThread.Distributteur;
import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;
import dz.nier.autre.utile.Utile;

class CreeSalonThread extends BasicThread {

	private CreeSalonForm creeSalonForm;
	private ClientBean client;

	public CreeSalonThread(Distributteur d, long outId,
			CreeSalonForm creeSalonForm, ClientBean client) {
		super(d);
		this.client = client;
		this.creeSalonForm = creeSalonForm;
		this.out_id = outId;
		this.setName("Creation Salon");
		this.start();
	}

	public void run() {
		out(new Requete(new RequestKeys[] { RequestKeys.RAISON,
				RequestKeys.OUT_ID, RequestKeys.SALON }, new Object[] {
				RequestValue.CREATION_SALON,
				this.id,
				new SalonBean(this.client, creeSalonForm.getNom().getText(),
						creeSalonForm.getSujet().getText(),
						(SalonBean.Type) creeSalonForm.getType()
								.getSelectedItem(), creeSalonForm.getPasswd()
								.getText()) }));
		Requete repence = in();
		if (!this.connected)
			return;
		if (repence.getAttribut(RequestKeys.REPENCE).equals(RequestValue.OK)) {
			new ConnectionSalonThread(this.distributeur,
					(SalonBean) repence.getAttribut(RequestKeys.SALON), client,
					this.out_id);
		} else
			Utile.showErrorDialog(
					(String) repence.getAttribut(RequestKeys.RAISON), "Erreur");
	}
}