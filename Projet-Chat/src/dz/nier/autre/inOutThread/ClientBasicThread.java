package dz.nier.autre.inOutThread;

import dz.nier.autre.request.RequestKeys;
import dz.nier.autre.request.RequestValue;
import dz.nier.autre.request.Requete;

public class ClientBasicThread extends BasicThread {

	protected final Long client_Id;

	public ClientBasicThread(Distributteur d, Long client_id) {
		super(d);
		this.client_Id = client_id;
		ClientBasicThreadCont.getInstance().addClientBasicThread(client_Id,
				this);
	}

	public ClientBasicThread(Distributteur d, Long outId, Long client_id) {
		super(d, outId);
		this.client_Id = client_id;
	}

	public void desconnect() {
		out(new Requete(RequestKeys.RAISON, RequestValue.DESCONECT));
		super.desconnect();
		ClientBasicThreadCont.getInstance().removeClientBasicThread(client_Id,
				this);
	}

	public Requete in() {
		Requete res = super.in();
		if (res == null)
			return null;
		if (res.contain(RequestKeys.RAISON)
				&& (res.getAttribut(RequestKeys.RAISON) instanceof RequestValue)
				&& ((RequestValue) res.getAttribut(RequestKeys.RAISON))
						.equals(RequestValue.DESCONECT))
			this.desconnect();
		else
			return res;
		return null;
	}
}
