package dz.nier.autre.utile;

import dz.nier.autre.inOutThread.ClientBasicThread;
import dz.nier.autre.inOutThread.Distributteur;

public abstract class ConnectionConfirmation extends ClientBasicThread {

	public ConnectionConfirmation(Distributteur d, String type, Long clientId) {
		super(d, clientId);
		this.id = this.out_id = 2;
		this.setName("confirmation connection " + type);
		this.setPriority(MAX_PRIORITY);
	}

	abstract protected void desconnected();

}
